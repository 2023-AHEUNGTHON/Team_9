package com.todayMohang.likelion.todayMohang.controller;

import com.todayMohang.likelion.todayMohang.domain.Bookmark;
import com.todayMohang.likelion.todayMohang.dto.BookmarkRequestDto;
import com.todayMohang.likelion.todayMohang.dto.BookmarkResponseDto;
import com.todayMohang.likelion.todayMohang.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    //즐겨찾기 추가
    @PostMapping("/add")
    public void addBookmark(@RequestBody BookmarkRequestDto bookmarkRequestDto){
        bookmarkService.addBookmark(bookmarkRequestDto);
    }

    //즐겨찾기 조회
    @GetMapping("/list")
    public List<BookmarkResponseDto> getLists(){
        return bookmarkService.getBookmarks();
    }

    //즐겨찾기 삭제
    @DeleteMapping("/delete/{postId}")
    public void deleteBookmark(@PathVariable("postId")Long postId){
        Bookmark bookmark = bookmarkService.findById(postId);
        bookmarkService.deleteBookmark(bookmark);
    }

}
