package com.minskim.springboots3cloudfrontex.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.minskim.springboots3cloudfrontex.dto.GalleryDto;
import com.minskim.springboots3cloudfrontex.service.GalleryService;
import com.minskim.springboots3cloudfrontex.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class GalleryController {
    private S3Service s3Service;
    private GalleryService galleryService;

    @GetMapping("/gallery")
    public String dispWrite(Model model) {
        List<GalleryDto> galleryDtoList = galleryService.getList();

        model.addAttribute("galleryList", galleryDtoList);

        return "/gallery";
    }

    @PostMapping("/gallery")
    public String execWrite(GalleryDto galleryDto, MultipartFile file) throws IOException {
        String imgPath = s3Service.upload(galleryDto.getFilePath(), file);
        galleryDto.setFilePath(imgPath);

        galleryService.savePost(galleryDto);

        return "redirect:/gallery";
    }

    @GetMapping("/profile")
    public Map<String, byte[]> getProfile() throws IOException {
        Map<String, byte[]> map = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        S3ObjectInputStream s3objectInputStream = s3Service.getProfile().getObjectContent();
        byte[] profile = null;
        try {
            profile = IOUtils.toByteArray(s3objectInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(profile.length);
        map.put("profile", profile);
        return map;
    }
}
