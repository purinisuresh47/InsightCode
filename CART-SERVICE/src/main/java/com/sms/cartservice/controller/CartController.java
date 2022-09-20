package com.sms.cartservice.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.cartservice.model.Cart;
import com.sms.cartservice.service.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {

	@Autowired
	public CartService cartService;

	@PostMapping("/{username}/{productId}")
	public String addToCart(@PathVariable long productId, @PathVariable String username) throws Exception {

		List<Cart> carts = cartService.findAllByUsername(username);

		if (!carts.isEmpty()) {
			for (Cart cart : carts) {
				if (cart.getProductId() == productId) {
					throw new Exception("Product already added in cart");
				}
			}
		}
		cartService.save(productId, username);
		return "Product added to cart sucessfully";

	}

	@GetMapping("/{username}")
	public List<Cart> getCartByUsername(@PathVariable String username) throws Exception {
		List<Cart> cart = cartService.findAllByUsername(username);
		if (!cart.isEmpty()) {
			return cart;
		}
		throw new Exception("No products available");
	}

	@DeleteMapping("/{username}/{productId}")
	public String deleteProductFromCart(@PathVariable long productId, @PathVariable String username) throws Exception {
		List<Cart> carts = cartService.findAllByUsername(username);
		if(!carts.isEmpty()) {
			for(Cart cart:carts) {
				if (cart.getProductId() == productId) {
					long id = cart.getId();
					cartService.remove(id);
				}
			}
			return "Product removed from cart";
		} else {
			throw new Exception("No products available in cart");
		}

	}
}
