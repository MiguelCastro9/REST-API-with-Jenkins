package com.api.integration;

import com.api.dto.request.PersonRequestDto;
import com.api.dto.response.PersonResponseDto;
import com.api.model.PersonModel;
import com.api.service.PersonService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonIntegrationTest {

    @Autowired
    PersonService personService;

    private List<PersonResponseDto> personResponseDto;

    @BeforeEach
    void cleanDatabase() {
        personService.deleteAll();
    }

    @Test
    void saveTest() {
        PersonRequestDto personRequestDto = new PersonRequestDto("miguel castro", new Date());
        personService.save(personRequestDto.convertPersonDtoForEntity());
        personResponseDto = personService.list().stream().map(person
                -> PersonResponseDto.convertEntityForPersonDto(person))
                .collect(Collectors.toList());
        Integer size = personResponseDto.size();
        Assertions.assertEquals(1, size);
    }

    @Test
    void listTest() {
        PersonRequestDto personRequestDto1 = new PersonRequestDto("miguel castro", new Date());
        PersonRequestDto personRequestDto2 = new PersonRequestDto("miguel castro", new Date());
        PersonRequestDto personRequestDto3 = new PersonRequestDto("miguel castro", new Date());
        personService.save(personRequestDto1.convertPersonDtoForEntity());
        personService.save(personRequestDto2.convertPersonDtoForEntity());
        personService.save(personRequestDto3.convertPersonDtoForEntity());
        personResponseDto = personService.list().stream().map(person
                -> PersonResponseDto.convertEntityForPersonDto(person))
                .collect(Collectors.toList());
        Integer size = personResponseDto.size();
        Assertions.assertEquals(3, size);
    }

    @Test
    void findTest() {
        PersonRequestDto personRequestDto = new PersonRequestDto("1", "miguel castro", new Date());
        personService.save(personRequestDto.convertPersonDtoForEntity());
        Optional<PersonModel> personModel = personService.find(personRequestDto.getId());
        Assertions.assertNotNull(personModel);
    }

    @Test
    void updateTest() {
        PersonRequestDto personRequestDto1 = new PersonRequestDto("miguel castro", new Date());
        PersonModel personModel = personService.save(personRequestDto1.convertPersonDtoForEntity());
        String personId = personModel.getId();
        Optional<PersonModel> existingPersonBeforeUpdate = personService.find(personId);
        Assertions.assertTrue(existingPersonBeforeUpdate.isPresent());
        PersonRequestDto personRequestDto2 = new PersonRequestDto("miguel updated", new Date());
        personService.update(personId, personRequestDto2.convertPersonUpdateDtoForEntity());
        Optional<PersonModel> existingPersonAfterUpdate = personService.find(personId);
        Assertions.assertTrue(existingPersonAfterUpdate.isPresent());
        Assertions.assertEquals("miguel updated", existingPersonAfterUpdate.get().getName());
    }

    @Test
    void deleteTest() {
        PersonRequestDto personRequestDto1 = new PersonRequestDto("miguel castro", new Date());
        PersonModel personModel = personService.save(personRequestDto1.convertPersonDtoForEntity());
        String personId = personModel.getId();
        Optional<PersonModel> existingPersonBeforeUpdate = personService.find(personId);
        Assertions.assertTrue(existingPersonBeforeUpdate.isPresent());
        personService.delete(personId);
        personResponseDto = personService.list().stream().map(person
                -> PersonResponseDto.convertEntityForPersonDto(person))
                .collect(Collectors.toList());
        Integer size = personResponseDto.size();
        Assertions.assertEquals(0, size);
    }
}
