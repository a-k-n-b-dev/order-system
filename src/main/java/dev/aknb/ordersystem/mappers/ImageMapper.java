package dev.aknb.ordersystem.mappers;

import dev.aknb.ordersystem.entities.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "token", expression = "java(getRandomToken())")
    @Mapping(target = "name", expression = "java(multipartFile.getOriginalFilename())")
    @Mapping(target = "extension", expression = "java(getExtension(multipartFile.getOriginalFilename()))")
    Image toFileStorage(MultipartFile multipartFile);

    default String getExtension(String imageName) {

        String extension = null;
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }
        int dot = imageName.lastIndexOf(".");
        if (dot > 0 && dot <= imageName.length() - 2) {
            extension = imageName.substring(dot + 1);
        }
        return extension;
    }

    default String getRandomToken() {
        return UUID.randomUUID().toString();
    }
}
