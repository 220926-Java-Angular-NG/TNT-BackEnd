package com.revature.dtos.login;

import com.revature.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private List<Product> wishList;

}
