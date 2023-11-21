package com.todayMohang.likelion.todayMohang.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    FESTIVAL("축제"),
    PUB("주점"),
    SHOW("전시공연"),
    ETC("기타");

    private final String name;

    @JsonValue
    public String getName(){
        return name;
    }
}
