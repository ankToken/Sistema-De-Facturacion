package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Bill;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Product;

public interface IClientService {

	List<Client> findAll();
	
	Page<Client> findAll(Pageable pageable);
	
	void save(Client client);
	
	Client findOne(Long id);
	
	void delete(Long id);
	
	List<Product> findByName(String term);
	
	void saveBill(Bill bill);
	
	Product findProductById(Long id);
	
	Bill findBillById(Long id);
	
	void deleteBill(Long id);
	
	Bill fetchByIdWithClientWithItemBillWithProduct(Long id);
	
	Client fetchByIdWithBills(Long id);
}
