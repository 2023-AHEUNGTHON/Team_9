package com.todayMohang.likelion.todayMohang.dto;

import com.todayMohang.likelion.todayMohang.domain.Image;
import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.type.Category;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostDetailResponseDto {

    private Long id;

    private String title;

    private String organizer;

    private LocalDateTime start;

    private LocalDateTime end;

    private Category category;

    private String content;

    private boolean bookmark; // 즐겨찾기 했는지 여부

    private List<String> imageUrls;

    public PostDetailResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.organizer = post.getOrganizer();
        this.start = post.getStart();
        this.end = post.getEnd();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.bookmark = false; // 수정하기
        this.imageUrls = new ArrayList<>();
        for(Image image : post.getImageList()) {
            imageUrls.add(image.getUrl());
        }
    }

}
