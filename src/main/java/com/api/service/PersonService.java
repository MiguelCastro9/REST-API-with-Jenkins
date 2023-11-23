package com.api.service;

import com.api.exception.MessageCustomException;
import com.api.model.PersonModel;
import com.api.repository.PersonRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel Castro
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public PersonModel save(PersonModel personModel) {
        personRepository.save(personModel);
        return personModel;
    }

    public List<PersonModel> list() {
        return personRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(PersonModel::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Optional<PersonModel> find(String id) {
        return personRepository.findById(id);
    }

    public PersonModel update(String id, PersonModel personModel) {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    existingPerson.setName(personModel.getName());
                    existingPerson.setBirth_date(personModel.getBirth_date());
                    return personRepository.save(existingPerson);
                })
                .orElseThrow(() -> new MessageCustomException("Person not found."));
    }

    public void delete(String id) {
        personRepository.findById(id)
                .ifPresentOrElse(
                        personModel -> {
                            personRepository.deleteById(id);
                        },
                        () -> {
                            throw new MessageCustomException("Person not found.");
                        }
                );
    }
}