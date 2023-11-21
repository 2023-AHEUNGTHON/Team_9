package com.todayMohang.likelion.todayMohang.dto;

import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.type.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class PostResponseDto {

    private Long id;

    private String title;

    private String organizer;

    private Date date;

    private Category category;

    private boolean bookmark; // 즐겨찾기 했는지 여부

    private String imageUrl;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.organizer = post.getOrganizer();
        this.date = post.getDate();
        this.category = post.getCategory();
        this.bookmark = false; // 수정하기
        if(post.getImageList().size() > 0) {
            this.imageUrl = post.getImageList().get(0).getUrl();
        } else {
            this.imageUrl = null;
        }
    }

}
