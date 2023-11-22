package com.todayMohang.likelion.todayMohang.service;

import com.todayMohang.likelion.todayMohang.dto.BookmarkRequestDto;
import com.todayMohang.likelion.todayMohang.domain.Bookmark;
import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.repository.BookmarkRepository;
import com.todayMohang.likelion.todayMohang.repository.PostRepository;
import com.todayMohang.likelion.todayMohang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public void addBookmark(BookmarkRequestDto bookmarkRequestDto){
        //일단 임의값으로 설정
        Long userId = 1L;
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(bookmarkRequestDto.getPostId()).orElseThrow();
        Bookmark bookmark = new Bookmark(user, post);
        bookmarkRepository.save(bookmark);

    }

}
