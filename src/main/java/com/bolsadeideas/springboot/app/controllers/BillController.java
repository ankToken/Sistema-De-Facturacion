package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Bill;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.ItemBill;
import com.bolsadeideas.springboot.app.models.entity.Product;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/bill")
@SessionAttributes("bill")
public class BillController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/form/{clientId}")
	public String save(@PathVariable(value = "clientId") Long clientId, Map<String, Object> model,
			RedirectAttributes flash) {

		Client client = clientService.findOne(clientId);

		if (client == null) {

			flash.addAttribute("Error", "Client doesnt exist");
			return "redirect:/list";
		}

		Bill bill = new Bill();
		bill.setClient(client);

		model.put("bill", bill);
		model.put("title", "Create bill");

		return "bill/form";
	}

	@GetMapping(value = "/charge-products/{term}", produces = {"application/json"})
	public @ResponseBody List<Product> chargeProducts(@PathVariable String term){
		return clientService.findByName(term);
	}

	@PostMapping("/form")
	public String save(@Valid Bill bill,
			BindingResult result,
			Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId, 
			@RequestParam(name = "quantity[]", required = false) Integer[] quantity, 
			RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "Create bill");
			return "bill/form";
		}
		
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("title", "Create bill");
			model.addAttribute("error", "Description can't be empty");
			return "bill/form";
		}
		
		for(int i = 0; i < itemId.length; i++) {
			Product product = clientService.findProductById(itemId[i]);
			
			ItemBill line = new ItemBill();
			line.setQuantity(quantity[i]);
			line.setProduct(product);
			
			bill.addItemBill(line);
		}
		
		clientService.saveBill(bill);
		
		status.setComplete();
		
		flash.addFlashAttribute("success", "Bill created successfully");
		
		return "redirect:/see/" + bill.getClient().getId();
	}
	
	@GetMapping("/see/{id}")
	public String see(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		
		Bill bill = clientService.fetchByIdWithClientWithItemBillWithProduct(id);
		
		if(bill == null) {
			flash.addAttribute("error", "It does'nt look good");
			return "redirect:/list";
		}
		
		model.addAttribute("title", "Bill: " + bill.getDescription());
		model.addAttribute("bill", bill);
		
		return "bill/see";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		
		Bill bill = clientService.findBillById(id);
		if(bill != null) {
			clientService.deleteBill(id);
			flash.addAttribute("success", "Bill deleted");
			return "redirect:/see/" + bill.getClient().getId();
		}
		
		flash.addAttribute("error", "Bill doesnt exist.");
		return "redirect:/list";
	}
}