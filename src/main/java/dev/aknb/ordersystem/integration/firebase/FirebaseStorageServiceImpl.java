package dev.aknb.ordersystem.integration.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    @Value("${firebase.download.url}")
    private String downloadUrl;

    @Value("${firebase.bucket.name}")
    private String bucketName;

    @Value("${firebase.project.id}")
    private String projectId;

    private StorageOptions storageOptions;

    @PostConstruct
    private void initializeFirebase() throws Exception {

        InputStream serviceAccount = getClass().getResourceAsStream("/certs/storage-c560f-firebase-adminsdk-5kw12-fb64aa2009.json");

        this.storageOptions = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
    }

    @Override
    public String getUrl(String name) {
        return String.format(downloadUrl, name);
    }

    @Override
    public String upload(File file, String fileName) throws IOException {

        Storage storage = storageOptions.getService();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        log.info("File {} uploaded to bucket {} as {}", fileName, bucketName, file.toPath());
        return String.format(downloadUrl, fileName);
    }

    @Override
    public File download(String fileName) throws IOException {

        Storage storage = storageOptions.getService();
        Blob blob = storage.get(BlobId.of(bucketName, fileName));

        Path path = Path.of(fileName);

        blob.downloadTo(path);
        return path.toFile();
    }
}

/*

public String uploadi(File image, String imageName) throws IOException {

        BlobId blobId = BlobId.of("gs://storage-c560f.appspot.com", imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("classpath:certs/storage-c560f-firebase-adminsdk-5kw12-fb64aa2009.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(image.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(imageName, StandardCharsets.UTF_8));
    }

*/