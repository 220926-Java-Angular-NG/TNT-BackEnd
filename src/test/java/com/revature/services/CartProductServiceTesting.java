package com.revature.services;

import com.revature.models.CartProduct;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repositories.CartProductRepository;
import org.hibernate.annotations.ColumnDefault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CartProductServiceTesting {

    @InjectMocks
    private CartProductService cartProductService;

    @Mock
    private CartProductRepository cartProductRepository;

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

     }

//     @BeforeEach
//     public void populateCart(){
//     }



    @Test
    public void givenCartProduct_createCartProduct_returnsGeneratedEntity(){
        Mockito.when(cartProductRepository.save(cartProduct1)).thenReturn(dbCartProduct1);

        CartProduct returnedCartProduct = cartProductService.createCartProduct(cartProduct1);

        Assertions.assertEquals(dbCartProduct1.getId(), returnedCartProduct.getId());
        Assertions.assertEquals(dbCartProduct1.getQuantity(), returnedCartProduct.getQuantity());
        Assertions.assertEquals(dbCartProduct1.getProduct().getId(), returnedCartProduct.getProduct().getId());
        Assertions.assertEquals(dbCartProduct1.getUser().getId(), returnedCartProduct.getUser().getId());

    }

    @Test
    public void givenUserId_findAllWithUserId_returnsListOfCartProducts(){
         Mockito.when(cartProductRepository.findAllByUserId(userId)).thenReturn(cart);

         List<CartProduct> returnedCart = cartProductService.findAllWithUserId(userId);



         for (int i=0; i<cart.size();i++){
             Assertions.assertEquals(cart.get(i).getId(), returnedCart.get(i).getId());
             Assertions.assertEquals(cart.get(i).getQuantity(), returnedCart.get(i).getQuantity());
             Assertions.assertEquals(cart.get(i).getProduct().getId(), returnedCart.get(i).getProduct().getId());
             Assertions.assertEquals(cart.get(i).getUser().getId(), returnedCart.get(i).getUser().getId());
         }

         Assertions.assertEquals(returnedCart.get(0).getUser().getId(), returnedCart.get(1).getUser().getId());
     }

     @Test
    public void givenCartProductAndQuantity_updateCartQuantity_returnsCartProduct(){
         CartProduct updatedCartProduct = cartProduct1;
         updatedCartProduct.setQuantity(quantityUpdate);
         CartProduct dBUpdatedCartProduct = dbCartProduct1;
         dBUpdatedCartProduct.setQuantity(quantityUpdate);

         Mockito.when(cartProductRepository.save(updatedCartProduct)).thenReturn(dBUpdatedCartProduct);

         CartProduct returnedCartProduct = cartProductService.updateCartQuantity(cartProduct1,quantityUpdate);

         Assertions.assertEquals(dbCartProduct1.getId(), returnedCartProduct.getId());
         Assertions.assertEquals(quantityUpdate, returnedCartProduct.getQuantity());
         Assertions.assertEquals(dbCartProduct1.getProduct().getId(), returnedCartProduct.getProduct().getId());
         Assertions.assertEquals(dbCartProduct1.getUser().getId(), returnedCartProduct.getUser().getId());

     }

     @Test
    public void givenCartId_deleteCart_returnsTrue(){
//         long invalidCartProductId = 6L;
         long validCartProductId = 1L;
//         Mockito.when(cartProductRepository.findById(invalidCartProductId)).thenReturn(null);
         Mockito.when(cartProductRepository.findById(validCartProductId)).thenReturn(Optional.of(dbCartProduct1));
//         Mockito.when(cartProductService.findCartById(invalidCartProductId)).thenReturn(null);
//         Mockito.when(cartProductService.findCartById(validCartProductId)).thenReturn(dbCartProduct1);


//         boolean resultInvalid = cartProductService.deleteCart(invalidCartProductId);
         boolean resultValid = cartProductService.deleteCart(validCartProductId);

//         Assertions.assertEquals(false, resultInvalid);
         Assertions.assertEquals(true, resultValid);
     }

     @Test
    public void givenUserId_clearCart_returnsTrue(){
         Mockito.when(cartProductRepository.deleteAllByUser_Id(userId)).thenReturn(1);

         boolean result = cartProductService.clearCart(userId);

         Assertions.assertEquals(true,result);
     }


}
