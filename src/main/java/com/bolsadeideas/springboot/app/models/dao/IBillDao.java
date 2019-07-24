package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Bill;

public interface IBillDao extends CrudRepository<Bill, Long>{

	@Query("select b from Bill b join fetch b.client c join b.items l join l.product where b.id=?1")
	Bill fetchByIdWithClientWithItemBillWithProduct(Long id);
}