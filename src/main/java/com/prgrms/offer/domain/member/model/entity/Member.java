package com.prgrms.offer.domain.member.model.entity;

import lombok.Builder;
import lombok.Getter;
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

    private String principal;

    private String provider;
    private String providerId;

    private String password;
    private String nickname;
    private String address;

    private String profileImage;

    private int appleLevel;


    protected Member() {
    }

    @Builder
    public Member(String principal, String password, String nickname, String address, String profileImage, int appleLevel
    , String provider, String providerId) {
        this.principal = principal;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.profileImage = profileImage;
        this.appleLevel = appleLevel;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, password))
            throw new IllegalArgumentException("Bad credential");
    }
}
