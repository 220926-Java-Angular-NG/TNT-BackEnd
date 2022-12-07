package com.revature.dtos.registration;

import com.revature.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<Product> wishList;

}
