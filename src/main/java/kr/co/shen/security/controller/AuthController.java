package kr.co.shen.security.controller;

import kr.co.shen.security.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Slf4j
@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth/form")
    public String form() {
        return "/auth/form";
    }

    @PostMapping("/auth/proc")
    public ResponseEntity<?> postProc(@RequestBody Map<String, Object> param) {
        String auth = (String) param.get("auth");
        authService.post(auth);
        return ResponseEntity.ok().build();
    }
}
