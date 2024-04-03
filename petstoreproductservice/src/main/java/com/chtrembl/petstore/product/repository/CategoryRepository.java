package com.chtrembl.petstore.product.repository;

import com.chtrembl.petstore.product.model.Category;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnBean(DataSource.class)
public interface CategoryRepository extends CrudRepository<Category, Long> {

}
