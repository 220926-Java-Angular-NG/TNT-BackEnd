package com.revature.repositories;

import com.revature.models.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findAllByUserId(int id);

    @Transactional
    Integer deleteAllByUser_Id(int userId);
}
