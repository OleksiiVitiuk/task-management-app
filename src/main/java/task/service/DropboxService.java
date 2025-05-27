package task.service;

import com.dropbox.core.DbxException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface DropboxService {
    String uploadFile(MultipartFile file) throws IOException, DbxException;

    void downloadById(String dropboxFileId);

    void deleteFile(String fileId) throws DbxException;
}
