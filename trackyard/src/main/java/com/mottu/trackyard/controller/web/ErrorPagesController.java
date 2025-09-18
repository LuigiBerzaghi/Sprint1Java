package com.mottu.trackyard.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPagesController {
  @GetMapping("/acesso-negado")
  public String acessoNegado() {
    return "fragments/acesso-negado"; // templates/acesso-negado.html
  }
}

