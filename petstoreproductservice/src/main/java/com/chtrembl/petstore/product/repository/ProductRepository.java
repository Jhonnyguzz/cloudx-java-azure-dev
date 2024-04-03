package com.chtrembl.petstore.product.repository;

import com.chtrembl.petstore.product.model.Product;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnBean(DataSource.class)
public interface ProductRepository extends CrudRepository<Product, Long> {

}
