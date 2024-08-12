package kr.co.shen.security.controller;

import kr.co.shen.security.dto.User;
import kr.co.shen.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @GetMapping("/join")
    public String join() {
        return "/user/join";
    }

    @PostMapping("/join/proc")
    public ResponseEntity<?> joinProc(@RequestBody @Validated(User.Post.class) User user, Errors bindingResults) {
        if (bindingResults.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResults.getAllErrors());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);

        userService.post(user);

        return ResponseEntity.ok(result);
    }
}
