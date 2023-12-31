package com.todayMohang.likelion.todayMohang.controller;

import com.todayMohang.likelion.todayMohang.domain.Bookmark;
import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.PostDetailResponseDto;
import com.todayMohang.likelion.todayMohang.dto.PostRequestDto;
import com.todayMohang.likelion.todayMohang.dto.PostResponseDto;
import com.todayMohang.likelion.todayMohang.dto.UserPostResponseDto;
import com.todayMohang.likelion.todayMohang.service.BookmarkService;
import com.todayMohang.likelion.todayMohang.service.PostServiceImpl;
import com.todayMohang.likelion.todayMohang.service.UserService;
import com.todayMohang.likelion.todayMohang.utils.DateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PostController {

    @Autowired
    PostServiceImpl postService;

    @Autowired
    UserService userService;

    @Autowired
    BookmarkService bookmarkService;

    @ApiOperation(value = "행사 생성 api", notes = "토큰 필요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "대학 인증 필요"),
            @ApiResponse(code = 404, message = "유저 정보 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/post/create")
    public ResponseEntity<?> createPost(@RequestPart(value = "data") PostRequestDto postRequestDto,
                                        @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                        Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                if(user.getAuthenticated()) {
                    postService.save(postRequestDto, files, user);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "행사 전체 조회 api", notes = "로그인 상태에서만 토큰 필요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "유저 정보 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPosts(Authentication authentication){
        try {
            List<Post> postList = postService.findAll();
            Comparator<Post> countComparator = Comparator.comparingLong(Post::getCount).reversed();
            postList.sort(countComparator);
            List<PostResponseDto> postResponseDtoList = postList.stream().map(PostResponseDto::new).collect(Collectors.toList());
            if(authentication == null) {
                return ResponseEntity.ok().body(postResponseDtoList);
            }
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                postResponseDtoList = checkBookmark(user, postList);
                return ResponseEntity.ok().body(postResponseDtoList);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "내 행사 조회 api", notes = "토큰 필요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "유저 정보 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "행사 수정 api", notes = "토큰 필요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "존재하지 않는 post id"),
            @ApiResponse(code = 403, message = "접근 권한 없음"),
            @ApiResponse(code = 404, message = "유저 정보 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "행사 삭제 api", notes = "토큰 필요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "존재하지 않는 post id"),
            @ApiResponse(code = 403, message = "접근 권한 없음"),
            @ApiResponse(code = 404, message = "유저 정보 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //상세조회
    @GetMapping("/post/{postId}")
    public PostDetailResponseDto detail(@PathVariable("postId") Long postId, Authentication authentication){
        postService.incrementCount(postId);
        Post post = postService.findById(postId).orElseThrow();
        PostDetailResponseDto detail = new PostDetailResponseDto(post);
        if(authentication != null) {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);
            if(userOptional.isPresent()) {
                List<Bookmark> bookmarks = bookmarkService.getBookmarks(userOptional.get());
                for(Bookmark bookmark : bookmarks) {
                    if(bookmark.getPost().equals(post)) {
                        detail.setBookmark(true);
                    }
                }
            }
        }
        return detail;
    }

    //날짜별 상세조회
    @GetMapping("/post/date/{date}")
    public ResponseEntity<List<PostResponseDto>> detailDate(@PathVariable("date") String dateStr, Authentication authentication){
        try {
            LocalDateTime date = DateUtil.parse(dateStr);
            List<Post> posts = postService.findByDate(date);
            Comparator<Post> countComparator = Comparator.comparingLong(Post::getCount).reversed();
            posts.sort(countComparator);
            List<PostResponseDto> postResponseDtos = new ArrayList<>();
            if(authentication == null) {
                postResponseDtos = posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
            } else {
                String email = authentication.getName();
                Optional<User> userOptional = userService.findByEmail(email);
                if(userOptional.isPresent()) {
                    postResponseDtos = checkBookmark(userOptional.get(), posts);
                }
            }
            return ResponseEntity.ok(postResponseDtos);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<PostResponseDto> checkBookmark(User user, List<Post> posts) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarks(user);
        List<Post> bookmarkedPosts = new ArrayList<>();
        for(Bookmark bookmark : bookmarks) {
            bookmarkedPosts.add(bookmark.getPost());
        }

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Post post : posts) {
            if(bookmarkedPosts.contains(post)) {
                postResponseDtoList.add(new PostResponseDto(post, true));
            } else {
                postResponseDtoList.add(new PostResponseDto(post, false));
            }
        }
        return postResponseDtoList;
    }

}
