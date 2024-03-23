package com.chtrembl.petstore.product.repository;


import com.chtrembl.petstore.product.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}
