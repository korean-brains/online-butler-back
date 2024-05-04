package com.koreanbrains.onlinebutlerback.repository.member;

import com.koreanbrains.onlinebutlerback.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
