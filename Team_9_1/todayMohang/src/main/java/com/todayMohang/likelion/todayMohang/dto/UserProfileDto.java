package com.todayMohang.likelion.todayMohang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserProfileDto {

    private String nickname;
    private Boolean authenticated;
}
