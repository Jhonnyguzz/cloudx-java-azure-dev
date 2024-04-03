package com.chtrembl.petstore.pet.repository;

import com.chtrembl.petstore.pet.model.Tag;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnBean(DataSource.class)
public interface TagRepository extends CrudRepository<Tag, Long> {

}
