package com.todayMohang.likelion.todayMohang.dto;

import com.todayMohang.likelion.todayMohang.type.Category;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime start;
    private LocalDateTime end;
    private String category;

    //생성자
    public BookmarkResponseDto(String title, String organizer, LocalDateTime start, LocalDateTime end, String content){
        this.title = title;
        this.organizer = organizer;
        this.start = start;
        this.end = end;
        this.category = category;
        this.content = content;
    }

}
