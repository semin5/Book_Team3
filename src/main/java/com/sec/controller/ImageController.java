package com.sec.controller;

import com.sec.entity.Image;
import com.sec.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) throws IOException {
        InputStream in = imageService.getImageStream(id);
        if (in == null) {
            return ResponseEntity.notFound().build();
        }

        Image image = imageService.getImageMetadata(id);

        byte[] bytes = in.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @PostMapping("/images/delete/{id}")
    public String deleteImage(@PathVariable String id,
                              @RequestHeader("referer") String referer) {
        Image image = imageService.getImageMetadata(id);
        if (image != null) {
            imageService.deleteImagesByPostId(image.getPostId());
        }

        return "redirect:" + referer;
    }

}
