package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false)  String error,
			@RequestParam(value = "logout", required = false)  String logout,
			Model model, Principal principal, RedirectAttributes flash) {
		
		if(principal != null) {
			flash.addFlashAttribute("Info", "You already have a session");
			return "redirect:/";
		}
		
		if(error != null) {
			model.addAttribute("Error", "User or password is wrong!!!");
		}
		
		if(error != null) {
			model.addAttribute("Success", "Closed successfully!!!");
		}
		
		return "login";
	}
}