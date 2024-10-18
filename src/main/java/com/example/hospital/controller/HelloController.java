package com.example.hospital.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/public/hello")
    public ResponseEntity<String> helloPublic() {
        return ResponseEntity.ok("Hello public user");
    }

    @GetMapping("/admin/hello")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("Hello admin");
    }

    @GetMapping("/head/hello")
    public ResponseEntity<String> helloHead() {
        return ResponseEntity.ok("Hello dear member");
    }

    @GetMapping("/user/hello")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("Hello");
    }


}
