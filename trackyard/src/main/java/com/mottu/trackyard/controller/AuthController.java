package com.mottu.trackyard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
  @GetMapping("/login")
  public String login() {
    return "fragments/login"; // corresponde a templates/fragments/login.html
  }
}

