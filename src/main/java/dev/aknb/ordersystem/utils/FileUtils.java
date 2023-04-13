package dev.aknb.ordersystem.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;

@UtilityClass
public class FileUtils {
    public static MediaType getMediaType(String extension) {
        switch (extension) {
            case "jpg", "jpeg" -> {

                return MediaType.IMAGE_JPEG;
            }
            case "png" -> {

                return MediaType.IMAGE_PNG;
            }
            default -> {
                return MediaType.IMAGE_GIF;
            }
        }
    }

//    public static File toFile(byte[] data, String filePath) throws IOException {
//        File file = new File(filePath);
//        FileOutputStream fos = new FileOutputStream(file);
//        fos.write(data);
//        fos.close();
//        return file;
//    }
}
