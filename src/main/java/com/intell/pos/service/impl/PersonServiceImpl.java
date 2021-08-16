package com.intell.pos.service.impl;

import com.intell.pos.domain.Person;
import com.intell.pos.repository.PersonRepository;
import com.intell.pos.service.PersonService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Person}.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person person) {
        log.debug("Request to save Person : {}", person);
        return personRepository.save(person);
    }

    @Override
    public Optional<Person> partialUpdate(Person person) {
        log.debug("Request to partially update Person : {}", person);

        return personRepository
            .findById(person.getId())
            .map(
                existingPerson -> {
                    if (person.getFirstName() != null) {
                        existingPerson.setFirstName(person.getFirstName());
                    }
                    if (person.getLastName() != null) {
                        existingPerson.setLastName(person.getLastName());
                    }
                    if (person.getCompanyName() != null) {
                        existingPerson.setCompanyName(person.getCompanyName());
                    }
                    if (person.getEmail() != null) {
                        existingPerson.setEmail(person.getEmail());
                    }
                    if (person.getPhone() != null) {
                        existingPerson.setPhone(person.getPhone());
                    }
                    if (person.getAddress() != null) {
                        existingPerson.setAddress(person.getAddress());
                    }
                    if (person.getPersonType() != null) {
                        existingPerson.setPersonType(person.getPersonType());
                    }
                    if (person.getProviousDue() != null) {
                        existingPerson.setProviousDue(person.getProviousDue());
                    }
                    if (person.getAccountNo() != null) {
                        existingPerson.setAccountNo(person.getAccountNo());
                    }
                    if (person.getCreatedAt() != null) {
                        existingPerson.setCreatedAt(person.getCreatedAt());
                    }
                    if (person.getUpdatedAt() != null) {
                        existingPerson.setUpdatedAt(person.getUpdatedAt());
                    }
                    if (person.getDeletedAt() != null) {
                        existingPerson.setDeletedAt(person.getDeletedAt());
                    }

                    return existingPerson;
                }
            )
            .map(personRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.deleteById(id);
    }
}
