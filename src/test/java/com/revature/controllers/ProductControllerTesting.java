package com.revature.controllers;

import com.revature.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductControllerTesting {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;

    @Test
    public void givenSession_getInventory_returnsResponseEntityOfListOfProducts(){

    }
}
