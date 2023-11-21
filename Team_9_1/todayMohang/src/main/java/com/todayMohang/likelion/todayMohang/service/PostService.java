package com.todayMohang.likelion.todayMohang.service;

import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.PostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    void save(PostRequestDto postRequestDto, List<MultipartFile> files, User user);

    List<Post> findByUser(User user);

    List<Post> findAll();

    void update(Post post);

    void delete(Post post);

}
