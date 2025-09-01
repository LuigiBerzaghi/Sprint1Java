package com.mottu.trackyard.controller.web;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motos")
public class MotosPageController {

    private final MotoService motoService;
    public MotosPageController(MotoService motoService) { this.motoService = motoService; }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<MotosDTO> lista = motoService.getAllMotos(PageRequest.of(page, size)); // <- usa seu service
        model.addAttribute("page", lista);
        return "motos/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        // Se o seu MotosDTO não tiver construtor vazio, crie com strings vazias:
        model.addAttribute("dto", new MotosDTO("", "", ""));
        model.addAttribute("isNew", true);
        return "motos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dto") MotosDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isNew", true);
            return "motos/form";
        }
        motoService.createMoto(dto);
        ra.addFlashAttribute("msgSuccess", "Moto criada com sucesso!");
        return "redirect:/motos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") String id, Model model) {
        MotosDTO dto = motoService.getMotoById(id);
        model.addAttribute("dto", dto);
        model.addAttribute("isNew", false);
        return "motos/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute("dto") MotosDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isNew", false);
            return "motos/form";
        }
        motoService.updateMoto(id, dto);
        ra.addFlashAttribute("msgSuccess", "Moto atualizada com sucesso!");
        return "redirect:/motos";
    }


    @GetMapping("/{id}/excluir")
    public String confirmDelete(@PathVariable("id") String id, Model model) {
        model.addAttribute("dto", motoService.getMotoById(id));
        model.addAttribute("isNew", false);
        return "motos/confirm-delete";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        motoService.deleteMoto(id);
        ra.addFlashAttribute("msgSuccess", "Moto excluída com sucesso!");
        return "redirect:/motos";
    }
}
