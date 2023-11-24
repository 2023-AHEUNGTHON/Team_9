package com.todayMohang.likelion.todayMohang.dto;

import com.todayMohang.likelion.todayMohang.domain.Bookmark;
import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.type.Category;
import com.todayMohang.likelion.todayMohang.utils.DateUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookmarkResponseDto {

    private Long id;
    private Long postId;
    private String title;
    private String organizer;
    private String start;
    private String end;
    private Category category;
    private boolean bookmark;
    private String imageUrl;

//    //생성자
//    public BookmarkResponseDto(String title, String organizer, LocalDateTime start, LocalDateTime end, String content){
//        this.title = title;
//        this.organizer = organizer;
//        this.start = start;
//        this.end = end;
//        this.category = category;
//        this.content = content;
//    }

    public BookmarkResponseDto(Bookmark bookmark) {
        this.id = bookmark.getId();
        Post post = bookmark.getPost();
        this.postId = post.getId();
        this.title = post.getTitle();
        this.organizer = post.getOrganizer();
        this.start = DateUtil.format(post.getStart());
        this.end = DateUtil.format(post.getEnd());
        this.category = post.getCategory();
        this.bookmark = true;
        if(post.getImageList().size() > 0) {
            this.imageUrl = post.getImageList().get(0).getUrl();
        } else {
            this.imageUrl = null;
        }
    }

}
