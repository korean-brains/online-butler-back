package com.koreanbrains.onlinebutlerback.common.entity;

import com.koreanbrains.onlinebutlerback.common.util.file.UploadFile;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadedFile {
    private String originalFilename;
    private String storeFilename;
    private String url;

    public UploadedFile(UploadFile uploadFile) {
        this.originalFilename = uploadFile.originalFilename();
        this.storeFilename = uploadFile.storeFilename();
        this.url = uploadFile.url();
    }
}
