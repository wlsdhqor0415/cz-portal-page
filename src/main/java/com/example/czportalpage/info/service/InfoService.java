package com.example.czportalpage.info.service;

import com.example.czportalpage.info.dto.InfoPostDto;
import com.example.czportalpage.info.dto.InfoRequestDto;
import com.example.czportalpage.info.entity.Info;
import com.example.czportalpage.info.entity.criteria;
import com.example.czportalpage.info.repository.InfoRepository;
import com.example.czportalpage.info.service.jsonParse.LevelData;
import com.example.czportalpage.info.service.jsonParse.userInfoRoot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.czportalpage.info.entity.criteria.Rating;
import static com.example.czportalpage.info.service.jsonParse.jsonFetcher.fetchJson;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoService {
    private final InfoRepository infoRepository;

    /**
     * 백준 아이디와 닉네임을 입력받아 Info 클래스 entity를 생성
     */
    public Long createInfo(InfoPostDto infoPostDto){
        log.info("createInfo has been started!!");
        // 1. `username`이 null이거나 비어 있다면 예외 발생
        if (infoPostDto.getUsername() == null || infoPostDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        // 2. `username` 중복 검사
        Optional<Info> existingInfo = infoRepository.findByUsername(infoPostDto.getUsername());
        if (existingInfo.isPresent()) {
            throw new UsernameAlreadyExistsException("Username '" + infoPostDto.getUsername() + "' already exists");
        }

        // 3. 새로운 Info 객체 생성 및 저장
        Info newInfo = new Info();
        newInfo.setUsername(infoPostDto.getUsername());
        newInfo.setNickname(infoPostDto.getNickname());

        try {
            String urlString = "https://solved.ac/api/v3/search/user?query=" + newInfo.getUsername();  // 사용하고자 하는 url로 변경
            String userInfo = fetchJson(urlString);
            ObjectMapper objectMapper = new ObjectMapper();
            try {

                userInfoRoot userInfoRoot = objectMapper.readValue(userInfo, userInfoRoot.class);
                newInfo.setInitRating(String.valueOf(userInfoRoot.getItems().get(0).getRating()));
                newInfo.setInitSolvedCount(String.valueOf(userInfoRoot.getItems().get(0).getSolvedCount()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String urlString = "https://solved.ac/api/v3/user/problem_stats?handle=" + newInfo.getUsername();  // 사용하고자 하는 url로 변경
            String levelData = fetchJson(urlString);
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                List<LevelData> levels = objectMapper.readValue(levelData, new TypeReference<List<LevelData>>() {});
                StringBuilder solvedCountByLevelArray = new StringBuilder();
                for (LevelData level : levels) {
                    solvedCountByLevelArray.append(level.getSolved()).append(",");
                }
                newInfo.setInitSolvedCountByLevelArray(solvedCountByLevelArray.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 저장소에 저장
        Info savedInfo = infoRepository.save(newInfo);

        return infoRepository.save(savedInfo).getInfoId();
    }

    /**
     * 1시간마다 유저의 current 정보를 업데이트
     */
    @Scheduled(fixedRate = 3600000)
    public void updateInfo(){
        log.info("updateInfo has been started!!");
        for(Info info : infoRepository.findAll()) {
            try {
                String urlString = "https://solved.ac/api/v3/search/user?query=" + info.getUsername();  // 사용하고자 하는 url로 변경
                String userInfo = fetchJson(urlString);
                ObjectMapper objectMapper = new ObjectMapper();
                try {

                    userInfoRoot userInfoRoot = objectMapper.readValue(userInfo, userInfoRoot.class);
                    info.setCurrentRating(String.valueOf(userInfoRoot.getItems().get(0).getRating()));
                    info.setCurrentSolvedCount(String.valueOf(userInfoRoot.getItems().get(0).getSolvedCount()));
                    log.info("solvedCount: {}", userInfoRoot.getItems().get(0).getSolvedCount());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String urlString = "https://solved.ac/api/v3/user/problem_stats?handle=" + info.getUsername();  // 사용하고자 하는 url로 변경
                String levelData = fetchJson(urlString);
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    List<LevelData> levels = objectMapper.readValue(levelData, new TypeReference<List<LevelData>>() {});
                    StringBuilder solvedCountByLevelArray = new StringBuilder();
                    for (LevelData level : levels) {
                        solvedCountByLevelArray.append(level.getSolved()).append(",");
                    }
                    info.setCurrentSolvedCountByLevelArray(solvedCountByLevelArray.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            infoRepository.save(info);
        }
    }

    /**
     * 서울 시각을 기준으로 매주 월요일 09:00마다 유저의 initial 정보를 업데이트
     * 매주 1, 2위 유저의 정보는 저장하고 유지
     * 이미 1, 2위 유저의 정보를 저장하는 테이블이 있을 때 중복해서 생성하는 것을 방지하는 코드는 구현되어 있지 않음
     */
    @Scheduled(cron = "0 0 9 * * MON", zone = "Asia/Seoul")
    public void resetInfoWeekly(String criteria){
        log.info("resetInfoWeekly has been started!!");

        // 1. 유저의 initial 정보를 업데이트
        for(Info info : infoRepository.findAll()) {
            info.setCurrentRating(info.getInitRating());
            info.setCurrentSolvedCount(info.getInitSolvedCount());
            info.setCurrentSolvedCountByLevelArray(info.getInitSolvedCountByLevelArray());
            infoRepository.save(info);
        }

        // 2. 1,2위 유저를 찾아 정보를 저장
        Info firstRankInfo, secondRankInfo;

        if(criteria.equals("rating")) {
            List<Info>infos = this.sortByCurrentRating();
            firstRankInfo = infos.get(infos.size()-1);
            secondRankInfo = infos.get(infos.size()-2);
        }
        else if(criteria.equals("solvedCount")) {
            List<Info>infos = this.sortByCurrentSolvedCount();
            firstRankInfo = infos.get(infos.size()-1);
            secondRankInfo = infos.get(infos.size()-2);
        }
        else if(criteria.equals("solvedCountByLevel")) {
            List<Info>infos = this.sortByCurrentSolvedCountByLevel();
            firstRankInfo = infos.get(infos.size()-1);
            secondRankInfo = infos.get(infos.size()-2);
        }
        else {
            throw new IllegalArgumentException("Invalid criteria");
        }

        infoRepository.save(firstRankInfo);
        infoRepository.save(secondRankInfo);
    }

    public InfoRequestDto findById(Long infoId){
        Info info = infoRepository.findById(infoId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Info with ID " + infoId + " not found"));
        return new InfoRequestDto(info);
    }

    public List<Info> sortByCurrentRating(){
        // 데이터베이스에서 모든 Info 엔티티 가져오기
        List<Info> infos = infoRepository.findAll();
        for(Info info : infos) { info.setCriteria(criteria.Rating); }

        infos.sort((o1, o2) -> {
            if (o1.getCurrentRating().isBlank()) return -1;
            if (o2.getCurrentRating().isBlank()) return 1;
            return o1.getCurrentRating().compareTo(o2.getCurrentRating());
        });
        return infos;
    }

    public List<Info> sortByCurrentSolvedCount(){
        // 데이터베이스에서 모든 Info 엔티티 가져오기
        List<Info> infos = infoRepository.findAll();
        for(Info info : infos) { info.setCriteria(criteria.SolvedCount); }

        infos.sort((o1, o2) -> {
            if (o1.getCurrentSolvedCount().isBlank()) return -1;
            if (o2.getCurrentSolvedCount().isBlank()) return 1;
            return o1.getCurrentSolvedCount().compareTo(o2.getCurrentSolvedCount());
        });
        return infos;
    }

    public List<Info> sortByCurrentSolvedCountByLevel(){
        // 데이터베이스에서 모든 Info 엔티티 가져오기
        List<Info> infos = infoRepository.findAll();
        for(Info info : infos) { info.setCriteria(criteria.SolvedCountByLevel); }

        infos.sort((o1, o2) -> {
            if (o1.getCurrentSolvedCountByLevelArray().isBlank()) return -1;
            if (o2.getCurrentSolvedCountByLevelArray().isBlank()) return 1;
            return o1.getCurrentSolvedCountByLevelArray().compareTo(o2.getCurrentSolvedCountByLevelArray());
        });
        return infos;
    }

    public List<InfoRequestDto> getAllInfos() {
        // 데이터베이스에서 모든 Info 엔티티 가져오기
        List<Info> infos = infoRepository.findAll();
        // List<Info>를 List<InfoDto>로 변환
        return infos.stream().map(InfoRequestDto::new).collect(Collectors.toList());
    }
}
