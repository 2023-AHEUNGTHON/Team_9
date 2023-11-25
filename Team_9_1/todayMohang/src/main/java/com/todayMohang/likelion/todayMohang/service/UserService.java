package com.todayMohang.likelion.todayMohang.service;

import com.todayMohang.likelion.todayMohang.domain.User;
import com.todayMohang.likelion.todayMohang.dto.UserLoginDto;
import com.todayMohang.likelion.todayMohang.dto.UserPostResponseDto;
import com.todayMohang.likelion.todayMohang.dto.UserProfileDto;
import com.todayMohang.likelion.todayMohang.dto.UserSignUpDto;
import com.todayMohang.likelion.todayMohang.exception.DataNotFoundException;
import com.todayMohang.likelion.todayMohang.jwt.JwtUtil;
import com.todayMohang.likelion.todayMohang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs=1000*60*60l;  //1시간

    @Transactional
    public void signUp(UserSignUpDto signUpDto){
        String email = signUpDto.getEmail();
        Optional<User> optUser=userRepository.findByEmail(email);
        if(optUser.isPresent()) {
            throw new DataNotFoundException("이메일이 중복되었습니다.");
        }else {
            User user = User.of(signUpDto);
            user.setAuthenticated(false);
            userRepository.save(user);
            return;
        }
    }

    public String login(UserLoginDto loginDto){
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        Optional<User> optUser = userRepository.findByEmailAndPassword(email, password);

        if (optUser.isPresent()){
            return JwtUtil.createJwt(email, secretKey, expiredMs);
        }else {
            throw new DataNotFoundException("사용자가 존재하지 않습니다.");
        }
    }

    public void checkUniv(String email, boolean check){
        Optional<User> optUser = userRepository.findByEmail(email);

        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setAuthenticated(check);
            userRepository.save(user);
        }else {
            throw new DataNotFoundException("사용자가 존재하지 않습니다.");
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public UserProfileDto getUserProfile(String email){
        Optional<User> optUser = userRepository.findByEmail(email);

        if (optUser.isPresent()){
            User user = optUser.get();
            String nickname = user.getNickname();
            boolean auth = user.getAuthenticated();

            UserProfileDto profileDto = new UserProfileDto();
            profileDto.setNickname(nickname);
            profileDto.setAuthenticated(auth);
            return profileDto;
        }else throw new DataNotFoundException("사용자가 존재하지 않습니다.");
    }

}
