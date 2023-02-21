package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}") // file.dir=C:/works/output_files/
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    // 파일을 여러개 업로드 하기도 해야 하니 여러개 업로드용 기능 추가
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile)); // MultipartFile을 저장소에 저장한 후, 원본명과 저장명을 담고 있는 UploadFile 오브젝트를 리턴한 것을 받는다. // 그걸 담아준다.
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    private static String createStoreFileName(String originalFilename) {
        // 서버에 저장할 파일명 : UUID 사용
        String uuid = UUID.randomUUID().toString(); // '유일한 이름값을 생성하는 UUID'
        // 보통 UUID를 사용해서 파일명으로 해서 서버에 저장하긴 하더라도, 보통 확장자는 붙여서 저장해준다. 왜냐하면 그래야 운영상 더 편리한 점이 있기 떄문이다.
        // 확장자를 뗴온다 -> originalFileName에서 캐내온다.
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);

    }

}
