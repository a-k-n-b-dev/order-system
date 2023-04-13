package dev.aknb.ordersystem.controllers;

import com.nimbusds.jose.util.BoundedInputStream;
import dev.aknb.ordersystem.config.ProjectConfig;
import dev.aknb.ordersystem.controllers.constants.ApiConstants;
import dev.aknb.ordersystem.dtos.ImageDto;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.Response;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.services.ImageService;
import dev.aknb.ordersystem.utils.FileUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Tag(name = "Image APIs")
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

    @GetMapping("/view/{token}")
    public ResponseEntity<?> view(@PathVariable("token") String token,
                                  @RequestHeader(value = "Range", required = false) String rangeHeader,
                                  @RequestHeader(value = "If-None-Match", required = false) String ifNonMatch) throws IOException {

        log.info("Rest request to view image token: {}", token);
        ImageDto imageDto = imageService.view(token);

        // Get the file size and Set the response headers
        long fileSize = imageDto.getData().length;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileUtils.getMediaType(imageDto.getExtension()));
        headers.setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic());

        // Set the ETag header to the MD5 hash of the file
        String eTag = String.format("\"%s\"", DigestUtils.md5Hex(imageDto.getData()));
        headers.setETag(eTag);

        if (eTag.equals(ifNonMatch)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_MODIFIED)
                    .headers(headers)
                    .build();
        }

        // If the request has a Range header, return a partial content response
        if (rangeHeader != null && rangeHeader.startsWith("bytes ")) {

            // Parse the Range header to get the byte range
            String[] rangeValue = rangeHeader.split("=")[1].split("-");

            long startByte = Long.parseLong(rangeValue[0]);
            long endByte = fileSize - 1;
            if (rangeValue.length > 1) {
                endByte = Long.parseLong(rangeValue[1]);
            }
            long contentLength = endByte - startByte + 1;

            // Set the content range header
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + startByte + "-" + endByte + "/" + fileSize);
            headers.setContentLength(contentLength);

            // Get the input stream of the resource and skip the initial bytes based on the start byte position
            InputStream inputStream = new ByteArrayInputStream(imageDto.getData());

            // Read and discard the first `rangeStart` bytes
            long skippedBytes = inputStream.skip(startByte);
            while (skippedBytes > 0) {
                skippedBytes -= inputStream.skip(skippedBytes);
            }

            // Return a partial content response with the input stream bounded by the specified byte range
            BoundedInputStream boundedInputStream = new BoundedInputStream(inputStream, contentLength);
            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(new InputStreamResource(boundedInputStream));
        }

        // If the request does not have a Range header, return a full content response
        headers.setContentLength(fileSize);
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageDto.getData());
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