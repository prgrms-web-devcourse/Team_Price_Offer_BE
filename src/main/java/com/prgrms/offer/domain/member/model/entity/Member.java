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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String principal;

    private String provider;
    private String providerId;

    private String password;
    private String nickname;
    private String address;

    private String profileImageUrl;

    @Builder.Default
    private int offerLevel = 1;

    private int score;

    protected Member() {
    }

    @Builder
    public Member(String principal, String password, String nickname, String address, String profileImageUrl, int offerLevel
    , int score, String provider, String providerId) {
        this.principal = principal;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.profileImageUrl = profileImageUrl;
        this.offerLevel = offerLevel;
        this.provider = provider;
        this.providerId = providerId;
        this.score = score;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, password))
            throw new IllegalArgumentException("Bad credential");
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public void decreaseScore(int score) {
        this.score -= score;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changeProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", appleLevel=" + offerLevel +
                '}';
    }
}
