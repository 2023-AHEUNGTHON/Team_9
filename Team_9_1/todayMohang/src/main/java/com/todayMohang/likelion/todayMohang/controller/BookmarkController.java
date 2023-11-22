package com.todayMohang.likelion.todayMohang.controller;

import com.todayMohang.likelion.todayMohang.dto.BookmarkRequestDto;
import com.todayMohang.likelion.todayMohang.dto.BookmarkResponseDto;
import com.todayMohang.likelion.todayMohang.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/add")
    public void addBookmark(@RequestBody BookmarkRequestDto bookmarkRequestDto){
        bookmarkService.addBookmark(bookmarkRequestDto);
    }

//    @GetMapping("/list")
//    public List<BookmarkResponseDto> getLists(){
//        return bookmarkService.getBookmarks();
//    }

}
