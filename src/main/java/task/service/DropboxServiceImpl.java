package task.repository;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import task.service.DropboxService;

@Service
@RequiredArgsConstructor
public class DropboxServiceImpl implements DropboxService {

    private static final String PATH_TO_DOWNLOAD = System.getProperty("user.home") + "/Downloads/";
    private static final int BUFFER_SIZE = 4096;
    private static final int END_FILE = -1;
    private static final int START_WITH_BITE = 0;

    private final DbxClientV2 client;

    @Override
    public String uploadFile(MultipartFile file) {
        String path = "/attachments/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        try (InputStream in = file.getInputStream()) {
            FileMetadata metadata = client.files().uploadBuilder(path)
                    .uploadAndFinish(in);
            return metadata.getId();
        } catch (IOException | DbxException e) {
            throw new DropboxProcessException(
                    "Can`t upload file: " + file.getOriginalFilename(), e);
        }
    }

    private InputStream downloadFile(String filePath) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            client.files().download(filePath).download(out);
        } catch (DbxException | IOException e) {
            throw new DropboxProcessException(
                    "Cant download file with path: " + filePath, e);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public void downloadById(String dropboxFileId) {
        try {
            Metadata metadata = client.files().getMetadata(dropboxFileId);

            if (metadata instanceof FileMetadata fileMetadata) {
                String path = fileMetadata.getPathLower();
                try (InputStream in = downloadFile(path);
                     FileOutputStream out = new FileOutputStream(
                             PATH_TO_DOWNLOAD + fileMetadata.getName())) {

                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != END_FILE) {
                        out.write(buffer, START_WITH_BITE, bytesRead);
                    }
                }
            }
        } catch (DbxException | IOException e) {
            throw new DropboxProcessException(
                    "Problem with downloading file with dropbox id: " + dropboxFileId, e);
        }
    }

    @Override
    public void deleteFile(String fileId) {
        try {
            client.files().deleteV2(fileId);
        } catch (DbxException e) {
            throw new RuntimeException("Cant delete file with id: " + fileId, e);
        }
    }
}
