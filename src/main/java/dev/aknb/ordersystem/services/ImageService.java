package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.entities.Image;
import dev.aknb.ordersystem.integration.firebase.FirebaseStorageService;
import dev.aknb.ordersystem.mappers.ImageMapper;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageService {

    private final FirebaseStorageService firebaseStorageService;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(FirebaseStorageService firebaseStorageService, ImageRepository imageRepository, ImageMapper imageMapper) {
        this.firebaseStorageService = firebaseStorageService;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public void upload(MultipartFile[] images, Long orderId) throws IOException {

        for (MultipartFile image : images) {
            upload(image, orderId);
        }
    }

    @Transactional
    public void upload(MultipartFile multipartFile, Long orderId) throws IOException {

        Image image = imageMapper.toFileStorage(multipartFile);
        image.setOrderId(orderId);
        String imageToken = String.format("%s.%s", image.getToken(), image.getExtension());
        File file = convertMultipartToFile(multipartFile, imageToken);
        String url = firebaseStorageService.upload(file, imageToken);
        image.setUrl(url);
        imageRepository.save(image);
    }

    private File convertMultipartToFile(MultipartFile file, String imageToken) throws IOException {

        File convertedFile = new File(imageToken);
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    public File view(String token) throws IOException {

        Optional<Image> image = imageRepository.findByToken(token);
        if (image.isEmpty()) {
            throw RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.IMAGE_NOT_FOUND.name());
        }
        String fileName = String.format("%s.%s", image.get().getToken(), image.get().getExtension());
        return firebaseStorageService.download(fileName);
    }
}
