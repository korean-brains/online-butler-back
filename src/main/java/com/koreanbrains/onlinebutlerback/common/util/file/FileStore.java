package com.koreanbrains.onlinebutlerback.common.util.file;

import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.IOException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;

@Component
public class FileStore {

    private final String filePath;
    private final String requestUrl;

    public FileStore(@Value("${file.path}") String filePath, @Value("${file.request-url}") String requestUrl) {
        this.filePath = filePath;
        this.requestUrl = requestUrl;
    }

    @PostConstruct
    public void createDirectory() {
        File directory = new File(filePath);
        if(!directory.exists()) {
            boolean isCreated = directory.mkdir();
            if(!isCreated) {
                throw new IllegalStateException("Failed to create directory: " + filePath);
            }
        }
    }

    public UploadFile upload(MultipartFile file, String storeName) {
        validationFile(file);

        String originalFilename = file.getOriginalFilename();
        String storeFilename = createStoreFileName(originalFilename, storeName);

        try {
            file.transferTo(new File(getFullPath(storeFilename)));
        } catch (Exception exception) {
            throw new IOException(exception, ErrorCode.FILE_NOT_UPLOADED);
        }

        return new UploadFile(originalFilename, storeFilename, getRequestUrl(storeFilename));
    }

    public Resource download(String filename) {
        try {
            return new UrlResource("file:" + getFullPath(filename));
        } catch (MalformedURLException e) {
            throw new IOException(e, ErrorCode.FILE_NOT_LOADED);
        }
    }

    public boolean delete(String filename) {
        return new File(getFullPath(filename)).delete();
    }

    public boolean hasFile(MultipartFile file) {
        return (file != null && !file.isEmpty());
    }

    private String getRequestUrl(String filename) {
        return requestUrl + "/" + filename;
    }

    private String getFullPath(String filename) {
        return filePath + filename;
    }

    private String createStoreFileName(String originalFilename, String storeName) {
        String ext = extractExt(originalFilename);
        return storeName + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private void validationFile(MultipartFile multipartFile) {
        if (!hasFile(multipartFile)) {
            throw new IOException(ErrorCode.FILE_INVALID);
        }
    }
}
