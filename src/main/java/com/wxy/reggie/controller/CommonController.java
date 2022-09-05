package com.wxy.reggie.controller;

import com.wxy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author wxy
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.image.path}")
    private String imagePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + suffix;

        File dir = new File(imagePath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            file.transferTo(new File(imagePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(filename);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(imagePath + name));

            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
