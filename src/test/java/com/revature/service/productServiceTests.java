package com.revature.service;

import com.revature.models.Product;
import com.revature.repositories.ProductRepository;
import com.revature.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class productServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private List<Product> products;
    private Product newProduct;
    private Product dbProduct;
    private Optional<Product> optDbProduct;






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
    }



    @Test
    public void givenNull_findAll_returnsListOfProducts(){
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> newProducts = productService.findAll();

        Assertions.assertEquals(products, newProducts);
    }

    @Test
    public void givenId_findById_returnsProductOptional(){
        Mockito.when(productRepository.findById(1)).thenReturn(optDbProduct);

        Optional<Product> gotProduct = productService.findById(1);

        Assertions.assertEquals(optDbProduct.get().getId(), gotProduct.get().getId());
        Assertions.assertEquals(optDbProduct.get().getQuantity(), gotProduct.get().getQuantity());
        Assertions.assertEquals(optDbProduct.get().getPrice(), gotProduct.get().getPrice());
        Assertions.assertEquals(optDbProduct.get().getDescription(), gotProduct.get().getDescription());
        Assertions.assertEquals(optDbProduct.get().getImage(), gotProduct.get().getImage());
        Assertions.assertEquals(optDbProduct.get().getName(), gotProduct.get().getName());
        Assertions.assertEquals(optDbProduct.get().isFeatured(), gotProduct.get().isFeatured());


    }


    @Test
    public void givenNewProduct_save_returnsCreatedEntity(){
        Mockito.when(productRepository.save(newProduct)).thenReturn(dbProduct);

        Product createdProduct = productService.save(newProduct);


        Assertions.assertEquals(dbProduct.getId(), createdProduct.getId());
        Assertions.assertEquals(dbProduct.getQuantity(), createdProduct.getQuantity());
        Assertions.assertEquals(dbProduct.getPrice(), createdProduct.getPrice());
        Assertions.assertEquals(dbProduct.getDescription(), createdProduct.getDescription());
        Assertions.assertEquals(dbProduct.getImage(), createdProduct.getImage());
        Assertions.assertEquals(dbProduct.getName(), createdProduct.getName());
        Assertions.assertEquals(dbProduct.isFeatured(), createdProduct.isFeatured());

    }








}
