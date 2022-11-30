package com.revature.dtos.registration;

import com.revature.models.Product;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class RegistrationResponse extends RegisterRequest{
    private String token = "";
    private List<Product> wishList = null;

    public RegistrationResponse(String email,String firstname,String lastname,String token,List<Product> wishlist){
        this.setEmail(email);
        this.setFirstName(firstname);
        this.setLastName(lastname);
        this.token = token;
        this.wishList = wishlist;
    }
}
