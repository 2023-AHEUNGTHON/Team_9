package com.todayMohang.likelion.todayMohang.service;

import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.PostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {

    void save(PostRequestDto postRequestDto, List<MultipartFile> files, User user);

    List<Post> findByUser(User user);

    List<Post> findAll();

    Optional<Post> findById(Long id);

    void update(Post post, List<MultipartFile> files);

    void delete(Post post);

}
