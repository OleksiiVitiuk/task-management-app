package task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import task.dto.AttachmentDto;
import task.service.AttachmentService;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
@Tag(name = "Attachments", description =
        "Endpoints for upload, download and delete files in cloud storage")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload file",
            description = "Upload and save file in cloud storage, "
                    + "save info about it in DB")
    public AttachmentDto upload(@RequestParam(value = "file") MultipartFile file,
                                @RequestParam("taskId") Long taskId) throws Exception {
        return attachmentService.uploadAttachment(file, taskId);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get attachments and download files",
            description = "Get and download files on PC(in download folder)")
    public Page<AttachmentDto> getAttachments(Pageable pageable,
                                              @RequestParam Long taskId) {
        return attachmentService.getAttachmentsByTask(pageable, taskId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete attachment",
            description = "Delete file from DB and cloud storage")
    public void deleteAttachment(@PathVariable Long id) throws Exception {
        attachmentService.deleteAttachment(id);
    }
}
