package com.koreanbrains.onlinebutlerback.entity.member;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

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

    @ColumnDefault("true")
    private boolean isActive;

    public void updateName(String name) {
        this.name = name;
    }

    public void disableMember() {
        this.isActive = false;
    }
}
