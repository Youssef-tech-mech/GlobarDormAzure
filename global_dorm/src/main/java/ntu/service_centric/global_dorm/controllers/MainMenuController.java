package ntu.service_centric.global_dorm.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;

@Controller
public class MainMenuController {

    @GetMapping("/menu")
    public ResponseEntity<String> showMainMenu() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/main-menu.html");
        String htmlContent = new String(Files.readAllBytes(resource.getFile().toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }
}
