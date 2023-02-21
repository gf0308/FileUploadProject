package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm itemForm) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        // 여기까지하면 일단 해당위치의 해당파일엔 진짜 저장이 되는데,
        // 파일저장만하는게 아니라 해당파일내용을 DB에 저장하는것도 해야 한다.
        // 데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);
        // 파일업로드 시
        // 파일 자체의 저장은 일반적으로 데이터베이스에 저장하지 않는다,
        // 파일 자체를 저장하는건 따로 파일을 저장해두기로 해놓은 스토리지(디스크 등 ex: AWS S3)에 저장하며, DB에 저장하는건 주로 파일의 '파일경로','파일명' 같은 걸 저장한다.
        // 파일경로 : 보통 파일의 '상대경로'를 저장함, 파일의 'Full Path' 를 다 저장할 때가 아예 없는건 아니지만 보통은 풀패스를 다 저장하기 보단 각 파일의 상대경로를 주로 저장한다 (전 파일들의 '공통경로'는 따로 정해놓고)

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));// new UrlResource(해당 파일의 풀패스); => 해당 url 위치에 존재하고 있는 자원(ex: 파일)에 실제 접근해서 그 데이터(스트림)를 가져온다.
    }

}
