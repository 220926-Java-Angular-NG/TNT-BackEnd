package com.revature.controllers;

import com.revature.services.CartProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CartProductControllerTesting {
    @InjectMocks
    private CartProductController cartProductController;
    @Mock
    private CartProductService cartProductService;


    @Test
    public void givenUserIdAndSession_getCartWithUserId_returnsListCartProducts(){
        
    }
}
