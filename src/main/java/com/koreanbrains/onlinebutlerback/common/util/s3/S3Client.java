package com.koreanbrains.onlinebutlerback.common.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
@RequiredArgsConstructor
public class S3Client {

    private final AmazonS3Client client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public UploadFile upload(MultipartFile file, String storeName) {
        String originalFilename = file.getOriginalFilename();
        String storeFilename = createStoreFileName(originalFilename, storeName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            client.putObject(new PutObjectRequest(bucket, storeFilename, file.getInputStream(), metadata));
        } catch (Exception exception) {
            throw new com.koreanbrains.onlinebutlerback.common.exception.IOException(exception, ErrorCode.FILE_NOT_UPLOADED);
        }

        return new UploadFile(originalFilename, storeFilename, client.getUrl(bucket, storeFilename).toString());
    }


    private String createStoreFileName(String originalFilename, String storeName) {
        String ext = extractExt(originalFilename);
        return storeName + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
