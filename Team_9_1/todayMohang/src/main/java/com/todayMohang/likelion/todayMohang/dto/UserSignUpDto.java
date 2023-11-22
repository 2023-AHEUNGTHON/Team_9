package com.todayMohang.likelion.todayMohang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDto {

    private String email;
    private String password;
    private String nickname;
}
