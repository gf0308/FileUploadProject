package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    // 이렇게 파일을 '구분'하는 이유는 최초 업로드 때의 파일명과 저장순간의 파일명은 달라져야 할 수 있기 때문이다.
    // 새 파일이 업로드되어 저장되려고 하는데
    // 기존에 저장되어 존재하고 있는 어떤 파일과 파일명이 동일한 상황이 발생할 수도 있다, 이렇게 되면 덮어씌워질 위험이 있다.
    // 이런 경우를 방지하기 위해 기존것들과 파일명 비교 등을 해서 파일명을 바꿔주거나 해야할 경우 바꿔줘야 하는데
    // 이와 같이 최초 업로드 때의 파일명과, 저장순간의 파일명은 달라질 수가 있다.

    // [정리] "고객이 업로드한 파읾여으로 서버 내부에 파일을 저장하면 안된다. 왜냐하면 서로 다른 고객이 같은 파일이름으로 업로드하는 경우 기존 파일 이름과 충돌이 날 수 있기 때문이다.
    // 서버에서는 저장할 파일명이 겹치지 않도록 '내부에서 관리하는 별도의 파일명'이 필요하다."
    private String uploadFileName;
    private String storeFileName; // 이 저장할 때의 파일명은 UUID 등을 활용해서 생성하기도 한다.

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this. storeFileName = storeFileName;
    }

}
