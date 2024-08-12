package kr.co.shen.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SampleController {

    @GetMapping("/{depth1}/{depth2}")
    public String test(@PathVariable String depth1,
                       @PathVariable(required = false) String depth2,
                       HttpServletRequest request) {

        return "request URI ::: " + request.getRequestURI();
    }
}
