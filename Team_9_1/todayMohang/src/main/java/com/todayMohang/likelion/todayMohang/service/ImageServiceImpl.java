package com.todayMohang.likelion.todayMohang.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.todayMohang.likelion.todayMohang.domain.Image;
import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String fileDir = System.getProperty("user.dir") + "/src/main/resources/files/"; // 로컬 환경 파일 경로
    //private final String fileDir = "/home/"; // 배포 환경 파일 경로

    public List<Image> uploadAll(List<MultipartFile> files, Post post) {
        try {
            List<Image> imageList = new ArrayList<>();
            for(MultipartFile file : files) {
                if(file != null && !file.isEmpty()) {
                    String url = upload(file);
                    System.out.println("uploaded " + url);
                    Image image = new Image(url, post);
                    imageList.add(image);
                    imageRepository.save(image);
                }
            }
            return imageList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String upload(MultipartFile file) {
        try {
            File uploadFile = convert(file)
                    .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

            String filename = "images/" + UUID.randomUUID() + uploadFile.getName();
            String uploadImageUrl = putS3(uploadFile, filename);
            removeNewFile(uploadFile);
            return uploadImageUrl;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String putS3(File uploadFile, String fileName) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void removeNewFile(File file) {
        try {
            if(file.delete()) {
                System.out.println("File delete success");
                return;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("File delete fail");
    }

    private Optional<File> convert(MultipartFile multipartFile) {
        try {
            if(multipartFile.isEmpty()) {
                return Optional.empty();
            }

            String originalFilename = multipartFile.getOriginalFilename();
            String storeFilename = createStoreFileName(originalFilename);

            File file = new File(fileDir + storeFilename);
            multipartFile.transferTo(file);

            return Optional.of(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private String createStoreFileName(String originalFilename) {
        return UUID.randomUUID() + "." + extractExt(originalFilename);
    }

    //확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
