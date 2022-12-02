package com.revature.controllers;

import com.revature.models.CartProduct;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.CartProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CartProductControllerTesting {
    @InjectMocks
    private CartProductController cartProductController;
    @Mock
    private CartProductService cartProductService;
    private CartProduct cartProduct1;
    private CartProduct cartProduct2;
    private CartProduct dbCartProduct1;
    private CartProduct dbCartProduct2;
    private List<CartProduct> cart;
    private Long cartProduct1Id = 1L;
    private Long cartProduct2Id = 2L;
    private int quantity = 2;
    private int quantityUpdate = 5;
    private Product product1;
    private Product product2;
    private User user1;
    private int userId = 1;
    private String email = "bigboy@sally.forth";
    private String password = "password";
    private String firstName = "Grimbo";
    private String lastName = "McFulstrom";
    private HttpSession session;


    @BeforeEach
    public void populateCartProduct(){
        product1 = new Product();
        product1.setId(1);
        product1.setQuantity(10);
        product1.setPrice(10.00);
        product1.setDescription("A good ol' time Jones");
        product1.setImage("imagelocation.jpg");
        product1.setName("Grek");
        product1.setFeatured(true);
        product2 = new Product();
        product2.setId(2);
        product2.setQuantity(8);
        product2.setPrice(19.89);
        product2.setDescription("Flattery whereabouts, tha?");
        product2.setImage("imagelocation2.jpg");
        product2.setName("Skronk");
        product2.setFeatured(false);

        user1 = new User();
        user1.setId(userId);
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setFirstName(firstName);
        user1.setLastName(lastName);

        cartProduct1 = new CartProduct();
        cartProduct1.setQuantity(quantity);
        cartProduct1.setProduct(product1);
        cartProduct1.setUser(user1);
        cartProduct2 = new CartProduct();
        cartProduct2.setQuantity(quantity);
        cartProduct2.setProduct(product2);
        cartProduct2.setUser(user1);

        dbCartProduct1 = new CartProduct();
        dbCartProduct1.setId(cartProduct1Id);
        dbCartProduct1.setQuantity(quantity);
        dbCartProduct1.setProduct(product1);
        dbCartProduct1.setUser(user1);
        dbCartProduct2 = new CartProduct();
        dbCartProduct2.setId(cartProduct2Id);
        dbCartProduct2.setQuantity(quantity);
        dbCartProduct2.setProduct(product2);
        dbCartProduct2.setUser(user1);

        cart = new ArrayList<>();
        cart.add(dbCartProduct1);
        cart.add(dbCartProduct2);

        session = new MockHttpSession();
        session.setAttribute("user", user1);

    }

    @Test
    public void givenCartProductAndSession_createCart_returnsResponseEntityOfCartProduct(){
        Mockito.when(cartProductService.createCartProduct(cartProduct1)).thenReturn(dbCartProduct1);

        ResponseEntity<CartProduct> responseEntity = cartProductController.createCart(cartProduct1, session);

        CartProduct returnedCartProduct = responseEntity.getBody();

        Assertions.assertEquals(dbCartProduct1.getId(), returnedCartProduct.getId());
        Assertions.assertEquals(dbCartProduct1.getQuantity(), returnedCartProduct.getQuantity());
        Assertions.assertEquals(dbCartProduct1.getProduct().getId(), returnedCartProduct.getProduct().getId());
        Assertions.assertEquals(dbCartProduct1.getUser().getId(), returnedCartProduct.getUser().getId());
        
    }
}
