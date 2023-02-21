package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        // multipart.enabled 설정이 true 이면(spring.servlet.multipart.enabled=true) 웹요청이 발생했을 시 DispatcherServlet과 관련한 내부 처리로 다음과 같이 처리가 일어난다.
        // : 웹요청이 전송되고 이를 받은 웹애플리케이션서버가(ex: Tomcat) HttpServletRequest 를 DispatcherServlet 으로 전달해주고
        //   DispatcherServlet 은 doDispatch 메서드를 실행하여 내부에서 checkMultipart(request) 를 처리하는데 (이 checkMultipart 메서드에서 'MultipartResolver' 를 호출하게 됨, 이게 해당요청이 멀티파트 형태인지 아닌지를 파악함)
        //   만약 해당 요청이 MultipartRequest 가 맞다면 MultipartHttpServletRequest 를 반환하게 된다. (To 컨트롤러)

        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        return "upload-form";
    }
}
