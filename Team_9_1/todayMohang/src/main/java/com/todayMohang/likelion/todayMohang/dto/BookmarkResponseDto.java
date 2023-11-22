package com.todayMohang.likelion.todayMohang.dto;

import com.todayMohang.likelion.todayMohang.type.Category;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class BookmarkResponseDto {
    //post id
    //post title
    //post content

    private Long id;
    private String title;
    private String organizer;
    private String content;
    private Date date;
    private String category;

    //생성자
    public BookmarkResponseDto(String title, String organizer, Date date,String content){
        this.title = title;
        this.organizer = organizer;
        this.date = date;
        this.category = category;
        this.content = content;
    }

}
