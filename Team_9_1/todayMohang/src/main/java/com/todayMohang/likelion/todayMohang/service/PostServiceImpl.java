package com.todayMohang.likelion.todayMohang.service;

import com.todayMohang.likelion.todayMohang.domain.Image;
import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.PostRequestDto;
import com.todayMohang.likelion.todayMohang.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    ImageServiceImpl imageService;

    @Override
    public void save(PostRequestDto postRequestDto, List<MultipartFile> files, User user) {
        try {
            //일단 포스트를 생성하고 그 다음에 이미지를 저장하고 포스트에 이미지를 넣어
            Post post = postRepository.save(new Post(postRequestDto, user));
            if(files != null) {
                List<Image> imageList = imageService.uploadAll(files, post);
                post.setImageList(imageList);
                postRepository.save(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> findByUser(User user) {
        try {
            return postRepository.findByUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Post> findAll() {
        try {
            return postRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Post post) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Post post) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
