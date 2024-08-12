package kr.co.shen.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        log.info(UUID.randomUUID().toString());
        return "/main";
    }
}
