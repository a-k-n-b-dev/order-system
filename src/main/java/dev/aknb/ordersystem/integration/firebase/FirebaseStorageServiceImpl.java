package dev.aknb.ordersystem.integration.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

@Slf4j
@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    @Value("${firebase.download.url}")
    private String downloadUrl;

    @Value("${firebase.bucket.name}")
    private String bucketName;

    @Value("${firebase.project.id}")
    private String projectId;
    @Value("${firebase.folder.path}")
    private String folderPath;

    private final RestTemplate restTemplate;

    private StorageOptions storageOptions;

    public FirebaseStorageServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    private void initializeFirebase() throws Exception {

        InputStream serviceAccount = new ClassPathResource("certs/storage-c560f-firebase-adminsdk-5kw12-fb64aa2009.json").getInputStream();

        this.storageOptions = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(
                        GoogleCredentials.fromStream(
                                Objects.requireNonNull(serviceAccount))).build();
    }

    @Override
    public String getUrl(String name) {
        return String.format(downloadUrl, name);
    }

    @Override
    public String upload(File file, String fileName) throws IOException {

        log.info("File {} uploading to bucket {} as {}", fileName, bucketName, file.toPath());
        Storage storage = storageOptions.getService();
        String fullPath = folderPath + fileName;
        BlobId blobId = BlobId.of(bucketName, fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        log.info("File {} uploaded to bucket {} as {}", fullPath, bucketName, file.toPath());
        return String.format(downloadUrl, folderPath + fileName);
    }

    @Override
    public byte[] download(String fileName) throws IOException {

        String fullPath = folderPath + fileName;

        String url = String.format(downloadUrl, fullPath);

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        return response.getBody();

    }
}

/*

//        return FileUtils.toFile(imageBytes, fileName);

public String upload(File image, String imageName) throws IOException {

        BlobId blobId = BlobId.of("gs://storage-c560f.appspot.com", imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("classpath:certs/storage-c560f-firebase-adminsdk-5kw12-fb64aa2009.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(image.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(imageName, StandardCharsets.UTF_8));
    }



    //        Storage storage = storageOptions.getService();
//        Blob blob = storage.get(BlobId.of(bucketName, fileName));
//        Path path = Path.of(fileName);
//
//        blob.downloadTo(path);
//        return path.toFile();

HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        HttpEntity<String> entity = new HttpEntity<>(headers);

*/