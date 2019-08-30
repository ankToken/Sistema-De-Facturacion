package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {

	@Autowired
	private IClientService clientService;
	
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/list-rest")
	public @ResponseBody List<Client> list(){
		return clientService.findAll();
	}
}
