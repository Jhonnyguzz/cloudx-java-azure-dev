package com.chtrembl.petstore.pet.repository;

import com.chtrembl.petstore.pet.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {

}
