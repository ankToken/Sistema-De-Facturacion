package com.bolsadeideas.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private IClientService clientService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Secured("ROLE_USER")
	@GetMapping(value = "/upload/{filename: .+}")
	public ResponseEntity<Resource> seePhoto(@PathVariable String fileName){
		Path pathPhoto = Paths.get("upload").resolve(fileName).toAbsolutePath();
		Resource resource = null;
		try {
			resource = new UrlResource(pathPhoto.toUri());
			if(!resource.exists() || !resource.isReadable()) {
				throw new RuntimeException("Cant charge image");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + resource.getFilename() + "\"").body(resource);
	}
	
	@Secured("ROLE_USER")
	@GetMapping(value = "see/{id}")
	public String see(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Client client = clientService.findOne(id);
		if(client == null) {
			flash.addFlashAttribute("error", "Client doesnt exist");
			return "redirect:/list";
		}
		model.put("client", client);
		model.put("title", "Client detail " + client.getName());
		return "see";
	}
	
	@RequestMapping(value = {"/list", "/"}, method = RequestMethod.GET)
	public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model
			, HttpServletRequest request) {
		
		Pageable pageRequest = PageRequest.of(page, 4);
		
		Page<Client> clients = clientService.findAll(pageRequest);
		
		PageRender<Client> pageRender = new PageRender<>("/list", clients);
		
		model.addAttribute("title", "Client list");
		model.addAttribute("clients", clients);
		model.addAttribute("page", pageRender);
		
		return "list";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String create(Map<String, Object> model) {
		Client client = new Client();
		model.put("title", "Client Form");
		model.put("client", client);
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String save(@Valid Client client, BindingResult result, Model model, @RequestParam("file") MultipartFile photo, RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "Client Form");
			flash.addFlashAttribute("error", "Client failed to add");
			return "form";
		}
		
		if(!photo.isEmpty()) {
			String rootPath = "/home/ricardo/upload";
			try {
				byte[] bytes = photo.getBytes();
				Path completePath = Paths.get(rootPath + "//" + photo.getOriginalFilename());
				Files.write(completePath, bytes);
				flash.addFlashAttribute("info: Photo uploaded correctly");
				client.setPhoto(photo.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String flashMessage = (client.getId() != null)?"Client updated successfully":"Client added successfully";
		clientService.save(client);
		status.setComplete();
		flash.addFlashAttribute("success", flashMessage);
		return "redirect:list";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String update(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Client client = null;
		
		if(id > 0) {
			client = clientService.findOne(id);
			if(client == null) {
				flash.addFlashAttribute("error", "Client doesnt exist");
				return "redirect:/list";
			}
		}
		else {
			flash.addFlashAttribute("error", "Client fail to update");
			return "redirect:/list";
		}
		
		model.put("title", "Update client");
		model.put("client", client);
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			Client client = clientService.findOne(id);
			clientService.delete(id);
			flash.addFlashAttribute("success", "Client deleted successfully");
			
			Path rootPath = Paths.get("upload").resolve(client.getPhoto()).toAbsolutePath();
			File file = rootPath.toFile();
			
			if(file.exists() && file.canRead()) {
				if(file.delete()) {
					flash.addAttribute("info", "Photo successfully deleted");
				}
			}
		}
		return "redirect:/list";
	}
	
	public boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null)
			return false;
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null)
			return false;
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
	}
}