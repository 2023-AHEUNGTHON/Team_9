package com.todayMohang.likelion.todayMohang.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class PostRequestDto {

    private String title;

    private String organizer;

    private Date date;

    private String category;

    private String content;

}
