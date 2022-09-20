package com.sms.cartservice.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.cartservice.model.Cart;
import com.sms.cartservice.repository.CartRepository;
import com.sms.cartservice.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Override
	public List<Cart> findAllByUsername(String username){
		List<Cart> cart = cartRepository.findAllByUsername(username);
		return cart;
	}

	@Override
	public Cart save(long productId, String username) {
		Cart cart = new Cart();
		cart.setProductId(productId);
		cart.setUsername(username);
		return cartRepository.save(cart);
	}

	@Override
	public void remove(long id) {
		cartRepository.deleteById(id);
	}

}
