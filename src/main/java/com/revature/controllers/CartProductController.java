package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.models.CartProduct;
import com.revature.models.User;
import com.revature.services.CartProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}, allowCredentials = "true")
public class CartProductController {

    private final CartProductService cartProductService;

    @GetMapping("/user/{userId}")
    @Authorized
    public List<CartProduct> getCartWithUserId(@PathVariable int userId, HttpSession session) {
        User currUser = (User)session.getAttribute("user");
        if (currUser == null || currUser.getId() != userId) return null;
        return cartProductService.findAllWithUserId(userId);
    }

    @PostMapping
    @Authorized
    public ResponseEntity<CartProduct> createCart(@RequestBody CartProduct cart, HttpSession session) {
        User currUser = (User)session.getAttribute("user");
        if (currUser == null) return null;
        cart.setUser(currUser);
        return ResponseEntity.ok(cartProductService.createCartProduct(cart));
    }

    @PutMapping("/item/{cartId}/quantity/{quantity}")
    @Authorized
    public CartProduct updateCartQuantity(@PathVariable Long cartId, @PathVariable int quantity, HttpSession session) {
        User currUser = (User)session.getAttribute("user");
        if (currUser == null) return null;
        CartProduct cartInfo = cartProductService.findCartById(cartId);
        if (cartInfo != null && cartInfo.getUser().getId() == currUser.getId())
            return cartProductService.updateCartQuantity(cartInfo, quantity);
        return null;
    }

    @DeleteMapping("/item/{cartId}")
    public boolean deleteCart(@PathVariable Long cartId) {
        return cartProductService.deleteCart(cartId);
    }

    @Authorized
    @DeleteMapping("/user/{userId}")
    public boolean clearUserCart(@PathVariable int userId, HttpSession session) {
        User currUser = (User)session.getAttribute("user");
        if (currUser == null) return false;
        if(currUser.getId() == userId) return false;
        return cartProductService.clearCart(userId);
    }


}
