package com.todayMohang.likelion.todayMohang.dto;

import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.type.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostDetailResponseDto {

    private Long id;

    private String title;

    private String organizer;

    private LocalDateTime start;

    private LocalDateTime end;

    private Category category;

    private String content;
    private boolean bookmark; // 즐겨찾기 했는지 여부

    private String imageUrl;

    public PostDetailResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.organizer = post.getOrganizer();
        this.start = post.getStart();
        this.end = post.getEnd();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.bookmark = false; // 수정하기
        if(post.getImageList().size() > 0) {
            this.imageUrl = post.getImageList().get(0).getUrl();
        } else {
            this.imageUrl = null;
        }
    }

}
