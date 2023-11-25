package com.todayMohang.likelion.todayMohang.domain;

import com.todayMohang.likelion.todayMohang.dto.UserSignUpDto;
import com.todayMohang.likelion.todayMohang.type.Category;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private Boolean authenticated;

    @Enumerated(EnumType.STRING)
    private Category interest;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    public static User of(UserSignUpDto userDto){
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .nickname(userDto.getNickname())
                .build();
    }
}
