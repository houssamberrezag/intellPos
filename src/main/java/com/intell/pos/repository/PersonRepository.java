package com.intell.pos.repository;

import com.intell.pos.domain.Person;
import com.intell.pos.domain.enumeration.PersonTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query(" select p from Person p where p.personType = ?1")
    Page<Person> findBypersonType(PersonTypes personType, Pageable pageable);
}
