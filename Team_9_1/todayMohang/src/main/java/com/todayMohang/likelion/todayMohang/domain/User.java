package com.todayMohang.likelion.todayMohang.domain;

import com.todayMohang.likelion.todayMohang.type.Category;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private boolean authenticated;

    @Enumerated(EnumType.STRING)
    private Category interest;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;

}