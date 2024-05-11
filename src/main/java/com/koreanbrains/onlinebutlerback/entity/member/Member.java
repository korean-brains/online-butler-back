package com.koreanbrains.onlinebutlerback.entity.member;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import com.koreanbrains.onlinebutlerback.common.entity.UploadedFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String introduction;

    @Builder.Default
    @ColumnDefault("'ROLE_USER'")
    private String role = "ROLE_USER";

    @Builder.Default
    @ColumnDefault("true")
    private boolean isActive = true;

    @Embedded
    private UploadedFile profileImage;

    public void updateName(String name) {
        this.name = name;
    }

    public void disableMember() {
        this.isActive = false;
    }

    public void updateProfileImage(UploadedFile profileImage) {
        this.profileImage = profileImage;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
