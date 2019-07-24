package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Product;

public interface IProductDao extends CrudRepository<Product, Long>{

	@Query("select p from Product p where p.name like %?1%")
	List<Product> findByName(String term);
}