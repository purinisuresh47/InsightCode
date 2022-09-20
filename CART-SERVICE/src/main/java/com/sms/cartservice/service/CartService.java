package com.sms.cartservice.service;

import java.util.List;

import com.sms.cartservice.model.Cart;

public interface CartService {

	List<Cart> findAllByUsername(String username);

	Cart save(long productId, String username);

	void remove(long id);

	

}
