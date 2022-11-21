package com.revature.services;

import com.revature.models.CartProduct;
import com.revature.repositories.CartProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository;

    public CartProduct findCartById(Long id) {
        return cartProductRepository.findById(id).orElseThrow(() -> new RuntimeException("Can Not find Cart Product with Id: " + id));
    }

    public CartProduct createCartProduct(CartProduct cartProduct) {
        return cartProductRepository.save(cartProduct);
    }

    public List<CartProduct> findAllWithUserId(int userId) {
        return cartProductRepository.findAllByUserId(userId);
    }

    public CartProduct updateCartQuantity(CartProduct cartProduct, int quantity) {
        cartProduct.setQuantity(quantity);
        return cartProductRepository.save(cartProduct);
    }

    public boolean deleteCart(Long cartId) {
        if(findCartById(cartId) == null) return false;
        cartProductRepository.deleteById(cartId);
        return true;
    }

    public boolean clearCart(int userId) {
        Integer itemsDeleted = cartProductRepository.deleteAllByUser_Id(userId);
        return itemsDeleted > 0;
    }

}
