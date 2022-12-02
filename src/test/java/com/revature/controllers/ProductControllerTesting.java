package com.revature.controllers;

import com.revature.models.Product;
import com.revature.services.ProductService;
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
import java.util.Optional;

@SpringBootTest
public class ProductControllerTesting {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;
    private List<Product> products;
    private Product newProduct;
    private Product dbProduct;
    private Product dbProduct2;
    private Optional<Product> optDbProduct;
    private HttpSession session;


    @BeforeEach
    public void populateProduct(){
        newProduct = new Product();
        newProduct.setId(1);
        newProduct.setQuantity(10);
        newProduct.setPrice(10.00);
        newProduct.setDescription("A good ol' time Jones");
        newProduct.setImage("imagelocation.jpg");
        newProduct.setName("Grek");
        newProduct.setFeatured(true);

        dbProduct = new Product();
        dbProduct.setId(1);
        dbProduct.setQuantity(10);
        dbProduct.setPrice(10.00);
        dbProduct.setDescription("A good ol' time Jones");
        dbProduct.setImage("imagelocation.jpg");
        dbProduct.setName("Grek");
        dbProduct.setFeatured(true);
        optDbProduct = Optional.ofNullable(dbProduct);


        dbProduct2 = new Product();
        dbProduct2.setId(2);
        dbProduct2.setQuantity(10);
        dbProduct2.setPrice(10.00);
        dbProduct2.setDescription("Frambidular crontstont");
        dbProduct2.setImage("imagelocation2.jpg");
        dbProduct2.setName("Blimbus");
        dbProduct2.setFeatured(true);

        products = new ArrayList<Product>();
        products.add(dbProduct);
        products.add(dbProduct2);

        session = new MockHttpSession();
    }


    @Test
    public void givenSession_getInventory_returnsResponseEntityOfListOfProducts(){
        Mockito.when(productService.findAll()).thenReturn(products);

        ResponseEntity<List<Product>> returnedList = productController.getInventory(session);

        Assertions.assertEquals(ResponseEntity.ok(products), returnedList);
    }

    @Test
    public void givenIdInt_getProductById_returnsResponseEntityProduct(){
        Mockito.when(productService.findById(1)).thenReturn(Optional.of(dbProduct));

        ResponseEntity<Product> returnedProduct = productController.getProductById(1);

        Assertions.assertEquals(dbProduct.getId(), returnedProduct.getBody().getId());
        Assertions.assertEquals(dbProduct.getQuantity(), returnedProduct.getBody().getQuantity());
        Assertions.assertEquals(dbProduct.getPrice(), returnedProduct.getBody().getPrice());
        Assertions.assertEquals(dbProduct.getDescription(), returnedProduct.getBody().getDescription());
        Assertions.assertEquals(dbProduct.getImage(), returnedProduct.getBody().getImage());
        Assertions.assertEquals(dbProduct.getName(), returnedProduct.getBody().getName());
    }

    @Test
    public void givenProduct_upsert_returnsResponseEntityProduct(){
        Mockito.when(productService.save(newProduct)).thenReturn(dbProduct);

        ResponseEntity<Product> returnedProduct = productController.upsert(newProduct);

        Assertions.assertEquals(dbProduct.getId(), returnedProduct.getBody().getId());
        Assertions.assertEquals(dbProduct.getQuantity(), returnedProduct.getBody().getQuantity());
        Assertions.assertEquals(dbProduct.getPrice(), returnedProduct.getBody().getPrice());
        Assertions.assertEquals(dbProduct.getDescription(), returnedProduct.getBody().getDescription());
        Assertions.assertEquals(dbProduct.getImage(), returnedProduct.getBody().getImage());
        Assertions.assertEquals(dbProduct.getName(), returnedProduct.getBody().getName());
    }



}
