package com.example.czportalpage.info.controller;

import com.example.czportalpage.info.dto.InfoPostDto;
import com.example.czportalpage.info.dto.InfoRequestDto;
import com.example.czportalpage.info.service.InfoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter @Setter
@RestController
@RequestMapping("/api/infos")
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @PostMapping("/post")
    public ResponseEntity postInfo(@RequestBody @Validated InfoPostDto infoPostDto){
        Long infoId = infoService.createInfo(infoPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(infoId);
    }

    @PatchMapping("/{infoId}")
    public ResponseEntity patchInfo(@PathVariable("infoId")Long infoId) {
        infoService.updateInfo();
        return ResponseEntity.status(HttpStatus.OK).body(infoId);
    }
    @GetMapping("/all")
    public ResponseEntity<List<InfoRequestDto>> getAllInfos() {
        List<InfoRequestDto> infos = infoService.getAllInfos();
        return ResponseEntity.status(HttpStatus.OK).body(infos);
    }

}