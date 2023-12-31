package com.todayMohang.likelion.todayMohang.domain;

import com.todayMohang.likelion.todayMohang.dto.PostRequestDto;
import com.todayMohang.likelion.todayMohang.type.Category;
import com.todayMohang.likelion.todayMohang.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String organizer;

    private LocalDateTime start;

    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String content;

    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @OneToMany(mappedBy = "post")
    private List<Image> imageList;

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.organizer = postRequestDto.getOrganizer();
        this.start = DateUtil.parse(postRequestDto.getStart());
        this.end = DateUtil.parse(postRequestDto.getEnd());
        this.category = Category.valueOf(postRequestDto.getCategory());
        this.content = postRequestDto.getContent();
    }

    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.organizer = postRequestDto.getOrganizer();
        this.start = DateUtil.parse(postRequestDto.getStart());
        this.end = DateUtil.parse(postRequestDto.getEnd());
        this.category = Category.valueOf(postRequestDto.getCategory());
        this.content = postRequestDto.getContent();
        this.count = 0L;
        this.user = user;
        this.imageList = new ArrayList<>();
    }

}