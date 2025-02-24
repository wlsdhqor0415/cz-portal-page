package com.example.czportalpage.info.dto;

//Info 객체를 반환할 때 사용하는 DTO

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
}
