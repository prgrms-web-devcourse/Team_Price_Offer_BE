package com.prgrms.offer.domain.member.repository;


import com.prgrms.offer.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPrincipal(String principal);

    @Query("select m from Member m where m.provider = :provider and m.providerId = :providerId")
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    Optional<Member> findByProviderId(String providerId);
}
