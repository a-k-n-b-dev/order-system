package dev.aknb.ordersystem.controllers;

import com.nimbusds.jose.util.BoundedInputStream;
import dev.aknb.ordersystem.config.ProjectConfig;
import dev.aknb.ordersystem.controllers.constants.ApiConstants;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.Response;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.services.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping(ApiConstants.API_IMAGE)
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @SecurityRequirement(name = ProjectConfig.NAME)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEV')")
    @RequestMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Response<String>> upload(@RequestParam("order-id") Long orderId,
                                                   @RequestPart("file") MultipartFile[] images) {

        log.info("Rest request to upload images to order id: {}", orderId);
        try {
            imageService.upload(images, orderId);
        } catch (IOException e) {
            throw RestException.restThrow(HttpStatus.BAD_REQUEST, MessageType.ERROR.name());
        }
        return ResponseEntity.ok(Response.ok("Images successfully uploaded"));
    }

    @PostMapping("/view/{token}")
    public ResponseEntity<?> view(@PathVariable("token") String token,
                                  @RequestHeader(value = "Range", required = false) String rangeHeader,
                                  @RequestHeader(value = "If-None-Match", required = false) String ifNonMatch,
                                  HttpServletResponse httpServletResponse) throws IOException {

        log.info("Rest request to view image token: {}", token);
        File file = imageService.view(token);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);

        long fileSize = file.length();

        if (rangeHeader == null || "".equals(rangeHeader)) {
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
            httpServletResponse.setHeader("Cache-Control", "max-age=86400"); // Cache for 24 hours
            String eTag = String.format("\"%s\"", DigestUtils.md5Hex(new FileInputStream(file)));
            httpServletResponse.setHeader("ETag", eTag);
            if (eTag.equals(ifNonMatch)) {
                return new ResponseEntity<>(null, headers, HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }

        String[] rangeHeaderArray = rangeHeader.split("=");
        String range = rangeHeaderArray[1];

        String[] rangeArray = range.split("-");
        long rangeStart = Long.parseLong(rangeArray[0]);
        long rangEnd = rangeStart + 1024 * 1024; // 1MB chunk size

        if (rangEnd > fileSize - 1) {
            rangEnd = fileSize - 1;
        }

        long rangeLength = rangEnd - rangeStart + 1;

        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength));
        headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangEnd + "/" + fileSize);
        InputStream inputStream = new FileInputStream(file);
        inputStream.skip(rangeStart);

        return new ResponseEntity<>(new InputStreamResource(new BoundedInputStream(inputStream, rangeLength)), headers, HttpStatus.PARTIAL_CONTENT);
    }
}


/*

@GetMapping("/view")
    public ResponseEntity<byte[]> viewImage(@RequestParam("token") String token,
                                            @RequestHeader(value = "If-None-Match", required = false) String ifNonMatch,
                                            HttpServletResponse httpServletResponse) throws IOException {

        File file = imageService.view(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        httpServletResponse.setHeader("Cache-Control", "max-age=86400"); // Cache for 24 hours
        String eTag = String.format("\"%s\"", DigestUtils.md5Hex(new FileInputStream(file)));
        httpServletResponse.setHeader("ETag", eTag);
        if (eTag.equals(ifNonMatch)) {
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, HttpStatus.OK);
    }

*/