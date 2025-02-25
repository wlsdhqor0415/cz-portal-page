package com.example.czportalpage.info.dto;

//Info 객체를 반환할 때 사용하는 DTO

import com.example.czportalpage.info.entity.Info;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String nickname;
    @NotBlank
    private String ratingDiff;
    @NotBlank
    private String solvedCountDiff;
    @NotBlank
    private String solvedCountDiffByLevelArray;

    public InfoRequestDto(Info info) {
        this.setUsername(info.getUsername());
        this.setNickname(info.getNickname());
        this.setRatingDiff(String.valueOf(
                Integer.parseInt(info.getCurrentRating()) - Integer.parseInt(info.getInitRating()))
        );
        this.setSolvedCountDiff(String.valueOf(
                Integer.parseInt(info.getCurrentSolvedCount()) - Integer.parseInt(info.getInitSolvedCount()))
        );

        ArrayList<Integer> currentSolvedCountByLevelArray = new ArrayList<Integer>();
        String[] currentItems = info.getCurrentSolvedCountByLevelArray().split(",");
        for (String item : currentItems) {
            currentSolvedCountByLevelArray.add(Integer.parseInt(item));
        }
        ArrayList<Integer> initSolvedCountByLevelArray = new ArrayList<Integer>();
        String[] initItems = info.getInitSolvedCountByLevelArray().split(",");
        for (String item : initItems) {
            initSolvedCountByLevelArray.add(Integer.parseInt(item));
        }
        String solvedCountByLevelArray = new String();
        for (int i = 0; i < currentSolvedCountByLevelArray.size(); i++) {
            solvedCountByLevelArray += String.valueOf(currentSolvedCountByLevelArray.get(i) - initSolvedCountByLevelArray.get(i)) + ",";
        }
        this.setSolvedCountDiff(solvedCountByLevelArray);
    }
}
