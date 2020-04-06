package com.wenmrong.community1.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

@Controller
public class WallPaperController {
    @GetMapping("/wallpaper")
    public String showImages() {

        return "download";
    }

    @GetMapping("/download/{filename}")
    public void download(@PathVariable("filename") String filename,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        String realpath = "http://cdn.wenmrong.com/" + filename;
        URL url = new URL(realpath);
        DataInputStream dataInputStream = new DataInputStream(url.openStream());
//        FileInputStream fileInputStream = new FileInputStream(realpath);
        int len = 0;
        byte[] buffer = new byte[1024];
        ServletOutputStream outputStream = response.getOutputStream();
        while ((len = dataInputStream.read(buffer)) > 0) {

            outputStream.write(buffer, 0, len);
        }
        dataInputStream.close();
        outputStream.close();


    }
}
