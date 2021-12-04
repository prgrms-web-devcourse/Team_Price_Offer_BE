package com.prgrms.offer.domain.member.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "members")
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String principal;

    private String password;
    private String nickname;
    private String address;

    private String profileImage;

    private int appleLevel;


    protected Member() {
    }

    @Builder
    public Member(String principal, String password, String nickname, String address, String profileImage, int appleLevel) {
        this.principal = principal;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.profileImage = profileImage;
        this.appleLevel = appleLevel;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, password))
            throw new IllegalArgumentException("Bad credential");
    }
}
