package com.koreanbrains.onlinebutlerback.common.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.koreanbrains.onlinebutlerback.common.exception.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URL;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class S3ClientTest {

    @InjectMocks
    S3Client s3Client;

    @Mock
    AmazonS3Client amazonS3Client;

    @Test
    @DisplayName("파일을 업로드한다")
    void upload() throws Exception {
        // given
        String storeName = "9257a629-f0a3-4fc4-8264-77baf3092644";
        MockMultipartFile image =
                new MockMultipartFile("images",
                        "cat.jpg",
                        MediaType.IMAGE_PNG_VALUE,
                        "<<image>>".getBytes());

        given(amazonS3Client.putObject(any())).willReturn(new PutObjectResult());
        given(amazonS3Client.getUrl(any(), anyString()))
                .willReturn(new URL("https://online-butler-s3.s3.ap-northeast-2.amazonaws.com/9257a629-f0a3-4fc4-8264-77baf3092644.jpg"));

        // when
        UploadFile uploadFile = s3Client.upload(image, storeName);

        // then
        assertThat(uploadFile.originalFilename()).isEqualTo("cat.jpg");
        assertThat(uploadFile.storeFilename()).isEqualTo("9257a629-f0a3-4fc4-8264-77baf3092644.jpg");
        assertThat(uploadFile.url()).isEqualTo("https://online-butler-s3.s3.ap-northeast-2.amazonaws.com/9257a629-f0a3-4fc4-8264-77baf3092644.jpg");
    }

    @Test
    @DisplayName("업로드에 실패하면 예외가 발생한다")
    void failUpload() {
        // given
        String storeName = "9257a629-f0a3-4fc4-8264-77baf3092644";
        MockMultipartFile image =
                new MockMultipartFile("images",
                        "cat.jpg",
                        MediaType.IMAGE_PNG_VALUE,
                        "<<image>>".getBytes());
        given(amazonS3Client.putObject(any())).willThrow(RuntimeException.class);

        // when
        // then
        assertThatThrownBy(() -> s3Client.upload(image, storeName))
                .isInstanceOf(IOException.class);
    }

}