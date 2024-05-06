package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.common.util.file.UploadFile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class FileFixture {

    public static MockMultipartFile multipartImage() {
        return new MockMultipartFile("images",
                "image.jpg",
                MediaType.IMAGE_PNG_VALUE,
                "<<image>>".getBytes());
    }

    public static MockMultipartFile multipartImage(String name) {
        return new MockMultipartFile(name,
                "image.jpg",
                MediaType.IMAGE_PNG_VALUE,
                "<<image>>".getBytes());
    }

    public static UploadFile uploadFile() {
        return new UploadFile("cat.jpg",
                "9257a629-f0a3-4fc4-8264-77baf3092644.jpg",
                "assets/9257a629-f0a3-4fc4-8264-77baf3092644.jpg");
    }
}
