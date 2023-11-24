package com.todayMohang.likelion.todayMohang.dto;

import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.type.Category;
import com.todayMohang.likelion.todayMohang.utils.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserPostResponseDto {

    private Long id;

    private String title;

    private String organizer;

    private String start;

    private String end;

    private Category category;

    private String imageUrl;

    public UserPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.organizer = post.getOrganizer();
        this.start = DateUtil.format(post.getStart());
        this.end = DateUtil.format(post.getEnd());
        this.category = post.getCategory();
        if(post.getImageList().size() > 0) {
            this.imageUrl = post.getImageList().get(0).getUrl();
        } else {
            this.imageUrl = null;
        }
    }

}
