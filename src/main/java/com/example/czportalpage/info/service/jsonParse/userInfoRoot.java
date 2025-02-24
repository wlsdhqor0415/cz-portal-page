package com.example.czportalpage.info.service.jsonParse;

import java.util.List;

public class userInfoRoot {
    private int count;
    private List<userInfoItem> userInfoItems;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<userInfoItem> getItems() {
        return userInfoItems;
    }

    public void setItems(List<userInfoItem> userInfoItems) {
        this.userInfoItems = userInfoItems;
    }
}

