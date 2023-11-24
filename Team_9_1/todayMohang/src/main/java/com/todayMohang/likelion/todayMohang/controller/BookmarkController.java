package com.todayMohang.likelion.todayMohang.controller;

import com.todayMohang.likelion.todayMohang.domain.Bookmark;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.BookmarkRequestDto;
import com.todayMohang.likelion.todayMohang.dto.PostResponseDto;
import com.todayMohang.likelion.todayMohang.service.BookmarkService;
import com.todayMohang.likelion.todayMohang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    private final UserService userService;

    //즐겨찾기 추가
    @PostMapping("/add")
    public void addBookmark(@RequestBody BookmarkRequestDto bookmarkRequestDto,
                            Authentication authentication){
        String email = authentication.getName();
        bookmarkService.addBookmark(bookmarkRequestDto, email);
    }

    //즐겨찾기 조회
    @GetMapping("/list")
    public ResponseEntity<List<PostResponseDto>> getLists(Authentication authentication){
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                List<Bookmark> bookmarks = bookmarkService.getBookmarks(userOptional.get());
                List<PostResponseDto> responseDtoList = new ArrayList<>();
                for(Bookmark bookmark : bookmarks) {
                    responseDtoList.add(new PostResponseDto(bookmark.getPost(), true));
                }
                return ResponseEntity.ok(responseDtoList);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //즐겨찾기 삭제
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable("postId")Long postId,
                                            Authentication authentication){
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                List<Bookmark> bookmarks = bookmarkService.getBookmarks(userOptional.get());
                for(Bookmark bookmark : bookmarks) {
                    if(bookmark.getPost().getId().equals(postId)) {
                        bookmarkService.deleteBookmark(bookmark);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
