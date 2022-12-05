package com.revature.controllers;

import com.revature.dtos.ProductInfo;
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
    private Product dbPurProduct;
    private Product dbPurProduct2;
    private Optional<Product> optDbProduct;
    private HttpSession session;
    private ProductInfo productInfo1;
    private ProductInfo productInfo2;
    private ProductInfo productInfoWrong;
    private ProductInfo productInfoTooMuch;
    private List<Product> productList;
    private List<ProductInfo> metadata;


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

        productInfo1 = new ProductInfo();
        productInfo1.setId(1);
        productInfo1.setQuantity(2);

        productInfo2 = new ProductInfo();
        productInfo2.setId(2);
        productInfo2.setQuantity(3);
        productInfoWrong = new ProductInfo();
        productInfoWrong.setId(6);
        productInfoWrong.setQuantity(10);
        productInfoTooMuch = new ProductInfo();
        productInfoTooMuch.setId(1);
        productInfoTooMuch.setQuantity(12);

        dbPurProduct = dbProduct;
        dbPurProduct.setQuantity(8);
        dbPurProduct2 = dbProduct2;
        dbPurProduct2.setQuantity(7);

        productList = new ArrayList<Product>();
        productList.add(dbPurProduct);
        productList.add(dbPurProduct2);

        metadata = new ArrayList<ProductInfo>();
        metadata.add(productInfo1);
        metadata.add(productInfo2);


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
    public void givenWrongIdInt_getProductById_returnsResponseEntityNotFound(){
        Mockito.when(productService.findById(6)).thenReturn(Optional.empty());

        ResponseEntity<Product> responseEntity = productController.getProductById(6);

        Assertions.assertEquals(ResponseEntity.notFound().build(), responseEntity);
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


    @Test
    public void givenListOfProductInfo_purchase_returnsResponseEntityOfListOfProducts(){
        Mockito.when(productService.findById(1)).thenReturn(optDbProduct);
        Mockito.when(productService.findById(2)).thenReturn(Optional.of(dbProduct2));
        Mockito.when(productService.saveAll(productList,metadata)).thenReturn(productList);


        ResponseEntity<List<Product>> responseEntity = productController.purchase(metadata);

        Assertions.assertEquals((dbProduct.getQuantity()), productList.get(0).getQuantity());
        Assertions.assertEquals((dbProduct2.getQuantity()), productList.get(1).getQuantity());
    }


    @Test
    public void givenInvalidListOfProductInfo_purchase_returnsResponseEntityOfBadRequest(){
        metadata.clear();
        metadata.add(productInfoWrong);

        Mockito.when(productService.findById(6)).thenReturn(Optional.empty());


        ResponseEntity<List<Product>> responseEntity = productController.purchase(metadata);

        Assertions.assertEquals(ResponseEntity.notFound().build(), responseEntity);
    }

    @Test
    public void givenListOfProductInfoTooMuch_purchase_returnsResponseEntityOfBadRequest(){
        metadata.clear();
        metadata.add(productInfoTooMuch);
        Mockito.when(productService.findById(1)).thenReturn(optDbProduct);


        ResponseEntity<List<Product>> responseEntity = productController.purchase(metadata);

        Assertions.assertEquals(ResponseEntity.badRequest().build(), responseEntity);
    }

    @Test
    public void givenProductId_deleteProduct_returnsMatchingProduct(){
        Mockito.when(productService.findById(1)).thenReturn(optDbProduct);

        ResponseEntity<Product> responseEntity = productController.deleteProduct(1);

        Assertions.assertEquals(1, responseEntity.getBody().getId());


    }



}
