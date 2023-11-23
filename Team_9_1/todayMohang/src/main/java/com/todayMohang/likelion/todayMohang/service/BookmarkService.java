package com.todayMohang.likelion.todayMohang.service;

import com.todayMohang.likelion.todayMohang.dto.BookmarkRequestDto;
import com.todayMohang.likelion.todayMohang.domain.Bookmark;
import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.BookmarkResponseDto;
import com.todayMohang.likelion.todayMohang.jwt.JwtUtil;
import com.todayMohang.likelion.todayMohang.repository.BookmarkRepository;
import com.todayMohang.likelion.todayMohang.repository.PostRepository;
import com.todayMohang.likelion.todayMohang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void addBookmark(BookmarkRequestDto bookmarkRequestDto, String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        Post post = postRepository.findById(bookmarkRequestDto.getPostId()).orElseThrow();
        Bookmark bookmark = new Bookmark(user, post);
        bookmarkRepository.save(bookmark);

    }

    public List<BookmarkResponseDto> getBookmarks(){
        Long userId = 1L;

        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);
        List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();
        for(Bookmark bookmark : bookmarks){
            BookmarkResponseDto bookmarkResponseDto = new BookmarkResponseDto(
                    bookmark.getPost().getTitle(),
                    bookmark.getPost().getContent(),
                    bookmark.getPost().getStart(),
                    bookmark.getPost().getEnd(),
                    bookmark.getPost().getOrganizer()
            );
            bookmarkResponseDtos.add(bookmarkResponseDto);
        }
        return bookmarkResponseDtos;
    }

    public Bookmark findById(Long id) {
        return bookmarkRepository.findById(id).orElseThrow();
    }
    public void deleteBookmark(Bookmark bookmark){
        bookmarkRepository.delete(bookmark);
    }

}
