package com.example.czportalpage.info.dto;

//객체를 새로 만들때 사용하는 DTO

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InfoPostDto {

    @NotBlank
    private String username;
    @NotBlank
    private String nickname;
}