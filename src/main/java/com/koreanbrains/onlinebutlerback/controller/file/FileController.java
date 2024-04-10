package com.koreanbrains.onlinebutlerback.controller.file;

import com.koreanbrains.onlinebutlerback.common.util.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;

    @ResponseBody
    @GetMapping("${file.request-url}/{filename}")
    public Resource downloadFile(@PathVariable("filename") String filename) {
        return fileStore.download(filename);
    }
}
