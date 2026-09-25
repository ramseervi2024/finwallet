package com.rps.finwallet.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/ui")
    public String index() {
        // This maps to src/main/resources/templates/index.html
        return "index";
    }

    @GetMapping("/login-password-encrypt")
    public String loginPasswordEncrypt() {
        return "login-password-encrypt";
    }

    @GetMapping("/file-upload")
    public String fileUpload() {
        return "file-upload";
    }

    @GetMapping("/payroll")
    public String payroll() {
        return "payroll";
    }
}
