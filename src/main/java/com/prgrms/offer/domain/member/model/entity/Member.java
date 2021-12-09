package com.prgrms.offer.domain.member.model.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

@Entity
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

    @Builder.Default
    private int appleLevel = 1;


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

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", principal='" + principal + '\'' +
                ", provider='" + provider + '\'' +
                ", providerId='" + providerId + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", appleLevel=" + appleLevel +
                '}';
    }
}
