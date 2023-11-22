package com.todayMohang.likelion.todayMohang.controller;

import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.PostRequestDto;
import com.todayMohang.likelion.todayMohang.dto.PostResponseDto;
import com.todayMohang.likelion.todayMohang.dto.UserPostResponseDto;
import com.todayMohang.likelion.todayMohang.service.PostServiceImpl;
import com.todayMohang.likelion.todayMohang.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PostController {

    @Autowired
    PostServiceImpl postService;

    @Autowired
    UserService userService;

    @Operation(summary = "", description = "")
    @PostMapping("/post/create")
    public ResponseEntity<?> createPost(@RequestPart(value = "data") PostRequestDto postRequestDto,
                                        @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                        Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                if(user.isAuthenticated()) {
                    postService.save(postRequestDto, files, user);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPosts(Authentication authentication){
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                List<Post> postList = postService.findAll();
                /**북마크 리스트 가져오기 -> 게시글 리스트랑 비교(postList.contains())해서 북마크인 애들은 dto에서 bookmark = true로 되게 하기*/
                List<PostResponseDto> postResponseDtoList = postList.stream().map(PostResponseDto::new).collect(Collectors.toList());
                return ResponseEntity.ok().body(postResponseDtoList);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/mypage/events")
    public ResponseEntity<List<UserPostResponseDto>> getPostsByUser(Authentication authentication){
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                List<Post> postList = postService.findByUser(userOptional.get());
                List<UserPostResponseDto> responseDtoList = postList.stream().map(UserPostResponseDto::new).collect(Collectors.toList());
                return ResponseEntity.ok().body(responseDtoList);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "", description = "")
    @PutMapping("/post/update/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId,
                                        @RequestPart(value = "data") PostRequestDto postRequestDto,
                                        @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                        Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                Optional<Post> postOptional = postService.findById(postId);
                if(postOptional.isPresent()) {
                    Post post = postOptional.get();
                    if(post.getUser().equals(userOptional.get())) {
                        post.update(postRequestDto);
                        postService.update(post, files);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "", description = "")
    @DeleteMapping("/post/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId,
                                        Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                Optional<Post> postOptional = postService.findById(postId);
                if(postOptional.isPresent()) {
                    Post post = postOptional.get();
                    if(post.getUser().equals(userOptional.get())) {
                        postService.delete(post);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //상세조회
    @GetMapping("/post/{postId}")
    public PostResponseDto detail(@PathVariable("postId") Long postId){
        Post post = postService.findById(postId).orElseThrow();
        return new PostResponseDto(post);
    }

    //날짜별 상세조회
    @GetMapping("/post/date/{date}")
    public List<PostResponseDto> detailDate(@PathVariable("date")Date date){
        List<Post> posts = postService.findByDate(date);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for(Post post : posts){
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }



}
