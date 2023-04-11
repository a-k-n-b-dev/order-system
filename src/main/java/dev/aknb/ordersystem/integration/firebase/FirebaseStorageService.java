package dev.aknb.ordersystem.integration.firebase;

import java.io.File;
import java.io.IOException;

public interface FirebaseStorageService {

    String getUrl(String name);

    String upload(File file, String fileName) throws IOException;

    File download(String fileName) throws IOException;
}
