package com.todayMohang.likelion.todayMohang.controller;


import com.todayMohang.likelion.todayMohang.dto.MailDto;
import com.todayMohang.likelion.todayMohang.dto.UserLoginDto;
import com.todayMohang.likelion.todayMohang.dto.UserProfileDto;
import com.todayMohang.likelion.todayMohang.dto.UserSignUpDto;
import com.todayMohang.likelion.todayMohang.service.UserService;
import com.univcert.api.UnivCert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
//@RequestMapping("/user")
@RequiredArgsConstructor
@Api(tags = "User", description = "User 관련 로직 작성")
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${univ.api-key}")
    private String univKey;

    @ApiOperation(value = "회원가입 api", notes = "토큰 필요 없음")
    @ApiResponses({
            @ApiResponse(code = 204, message = "성공"),
            @ApiResponse(code = 400, message = "실패")
    })
    @PostMapping("/user/signup")
    public ResponseEntity<Void> signUp(@RequestBody UserSignUpDto signUpDto){
        try{
            userService.signUp(signUpDto);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ApiOperation(value = "로그인 api", notes = "토큰 필요 없음")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "실패")
    })
    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto){
        try {
            String token = userService.login(loginDto);
            return new ResponseEntity<String>(token, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ApiOperation(value = "대학 메일 인증 코드 발송", notes = "대학교 이메일, 대학교 이름만 주면 됩니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "실패")
    })@PostMapping("/user/univcert.com/api/v1/certify")
    public ResponseEntity<?> sendUnivCertMail(@RequestBody MailDto mailDto){
        boolean univ_check = false; // 디폴트
        try {
            Map<String, Object> check = UnivCert.check(mailDto.getUnivName());
            if((boolean) check.get("success")){
                univ_check = true;
            }
            Map<String, Object> response = UnivCert.certify(univKey, mailDto.getUniv_email(), mailDto.getUnivName(), univ_check);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ApiOperation(value = "인증 코드 입력", notes = "대학교 이메일, 대학교 이름, 인증 코드 필수(1000~9999)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "실패")
    })@PostMapping("/univcert.com/api/v1/certifycode")
    public ResponseEntity<?> receiveUnivCertMail(Authentication authentication,
                                                 @RequestBody MailDto mailDto){
        try {
            Map<String, Object> response = UnivCert.certifyCode(univKey, mailDto.getUniv_email(), mailDto.getUnivName(), mailDto.getCertCode());
            boolean success = (boolean) response.get("success");
            if (success){
                String email = authentication.getName();
                userService.checkUniv(email, success);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ApiOperation(value = "개인페이지", notes = "닉네임, 인증 여부 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "실패")
    })@GetMapping("/user/myprofile")
    public ResponseEntity<UserProfileDto> getUserProfile(Authentication authentication){

        try {
            String email = authentication.getName();
            UserProfileDto profileDto = userService.getUserProfile(email);
            return new ResponseEntity<>(profileDto, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
