package com.sec.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sec.entity.Image;
import com.sec.repository.mongo.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final GridFsTemplate gridFsTemplate;
    private final ImageRepository imageRepository;

    public String storeImage(MultipartFile file, int postId) throws IOException {
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());

        Image image = Image.builder()
                .id(fileId.toString())
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .uploadDate(LocalDateTime.now())
                .postId(postId)
                .build();

        imageRepository.save(image);

        return fileId.toString();
    }

    public Image getImageMetadata(String id) {
        return imageRepository.findById(id).orElse(null);
    }

    public InputStream getImageStream(String id) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(
                query(where("_id").is(new ObjectId(id))));

        if (file == null) {
            return null;
        }

        GridFsResource resource = gridFsTemplate.getResource(file);

        return resource.getInputStream();
    }

    public List<Image> getImagesByPostId(Integer postId) {

        return imageRepository.findByPostId(postId);
    }

    public void deleteImagesByPostId(Integer postId) {

        List<Image> images = imageRepository.findByPostId(postId);

        for (Image image : images) {
            gridFsTemplate.delete(
                    query(where("_id").is(new ObjectId(image.getId()))));

            imageRepository.delete(image);
        }
    }
}