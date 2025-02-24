package com.example.czportalpage.info.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoId;

    private String username;

    private String nickname;

    private String initRating;

    private String currentRating;

    private String initSolvedCount;

    private String currentSolvedCount;

    private String initSolvedCountByLevelArray;

    private String currentSolvedCountByLevelArray;
}
