package com.example.czportalpage.info.service.jsonParse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class userInfoItem {

    @Getter @Setter
    private String handle;
    @Getter @Setter
    private String bio;
    @Getter @Setter
    private boolean verified;
    @Getter @Setter
    private Integer badgeId;
    @Getter @Setter
    private String backgroundId;
    @Getter @Setter
    private String profileImageUrl;
    @Getter @Setter
    private int solvedCount;
    @Getter @Setter
    private int voteCount;
    private int userClass;
    @Getter @Setter
    private String classDecoration;
    @Getter @Setter
    private int rivalCount;
    @Getter @Setter
    private int reverseRivalCount;
    @Getter @Setter
    private int tier;
    @Getter @Setter
    private int rating;
    @Getter @Setter
    private int ratingByProblemsSum;
    @Getter @Setter
    private int ratingByClass;
    @Getter @Setter
    private int ratingBySolvedCount;
    @Getter @Setter
    private int ratingByVoteCount;
    @Getter @Setter
    private int arenaTier;
    @Getter @Setter
    private int arenaRating;
    @Getter @Setter
    private int arenaMaxTier;
    @Getter @Setter
    private int arenaMaxRating;
    @Getter @Setter
    private int arenaCompetedRoundCount;
    @Getter @Setter
    private int maxStreak;
    @Getter @Setter
    private int coins;
    @Getter @Setter
    private int stardusts;
    @Getter @Setter
    private String joinedAt;
    @Getter @Setter
    private String bannedUntil;
    @Getter @Setter
    private String proUntil;
    @Getter @Setter
    private int rank;

    @JsonProperty("class")
    public int getUserClass() {
        return userClass;
    }

    @JsonProperty("class")
    public void setUserClass(int userClass) {
        this.userClass = userClass;
    }
}