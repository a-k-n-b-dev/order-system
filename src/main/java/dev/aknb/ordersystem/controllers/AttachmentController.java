package dev.aknb.ordersystem.controllers;

import dev.aknb.ordersystem.services.AttachmentService;
import dev.aknb.ordersystem.constants.ApiConstants;
import dev.aknb.ordersystem.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(ApiConstants.API_ATTACHMENT)
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Response<String>> upload(@RequestPart("file")MultipartFile[] files) {
        log.info("Rest request to upload files");
        attachmentService.upload(files);
        return ResponseEntity.ok(Response.ok("Files successfully uploaded"));
    }
}
