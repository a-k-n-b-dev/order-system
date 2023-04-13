package dev.aknb.ordersystem.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private String url;
    private String extension;
    private byte[] data;
}
