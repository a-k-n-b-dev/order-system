package dev.aknb.ordersystem.integration.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseStorageConfig {

    @Value("${firebase.bucket.name}")
    private String bucketName;

    @Bean
    public FirebaseApp storageOptions() throws IOException {

        InputStream serviceAccount = new ClassPathResource("classpath:certs/storage-c560f-firebase-adminsdk-5kw12-fb64aa2009.json").getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(bucketName)
                .build();
        return FirebaseApp.initializeApp(options);
    }
}
