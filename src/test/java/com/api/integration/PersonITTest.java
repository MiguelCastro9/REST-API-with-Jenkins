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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonITTest {

    @Autowired
    PersonService personService;

    @Autowired
    TestRestTemplate testRestTemplate;

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
        Optional<PersonModel> existingPersonBeforeDelete = personService.find(personId);
        Assertions.assertTrue(existingPersonBeforeDelete.isPresent());
        personService.delete(personId);
        personResponseDto = personService.list().stream().map(person
                -> PersonResponseDto.convertEntityForPersonDto(person))
                .collect(Collectors.toList());
        Integer size = personResponseDto.size();
        Assertions.assertEquals(0, size);
    }

    @Test
    void savePostTest() {
        PersonRequestDto personRequestDto = new PersonRequestDto("miguel castro", new Date());
        ResponseEntity<PersonRequestDto> requestTemplate = testRestTemplate.postForEntity("/person/save", personRequestDto, PersonRequestDto.class);
        Assertions.assertNotNull(requestTemplate);
    }

    @Test
    void listGetTest() {
        PersonRequestDto personRequestDto = new PersonRequestDto("miguel castro", new Date());
        ResponseEntity<PersonRequestDto> requestTemplate = testRestTemplate.postForEntity("/person/save", personRequestDto, PersonRequestDto.class);
        Assertions.assertNotNull(requestTemplate);
        ResponseEntity<List<PersonResponseDto>> responseTemplate = testRestTemplate.exchange(
                "/person/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PersonResponseDto>>() {
        }
        );
        Assertions.assertNotNull(responseTemplate);
        List<PersonResponseDto> personResponseDtoList = responseTemplate.getBody();
        Assertions.assertNotNull(personResponseDtoList);
        Assertions.assertFalse(personResponseDtoList.isEmpty());
    }

    @Test
    void findGetTest() {
        PersonRequestDto personRequestDto = new PersonRequestDto("miguel castro", new Date());
        PersonModel personModel = personService.save(personRequestDto.convertPersonDtoForEntity());
        String personId = personModel.getId();
        Optional<PersonModel> findPerson = personService.find(personId);
        Assertions.assertNotNull(findPerson);
        ResponseEntity<?> personTemplate = testRestTemplate.getForEntity("/person/find/" + personId, Object.class);
        Assertions.assertNotNull(personTemplate);
    }

    @Test
    void updatePutTest() {
        PersonRequestDto personRequestDto1 = new PersonRequestDto("miguel castro", new Date());
        PersonModel personModel = personService.save(personRequestDto1.convertPersonDtoForEntity());
        String personId = personModel.getId();
        Optional<PersonModel> existingPersonBeforeUpdate = personService.find(personId);
        Assertions.assertTrue(existingPersonBeforeUpdate.isPresent());
        PersonRequestDto personRequestDto2 = new PersonRequestDto("miguel updated", new Date());
        ResponseEntity<Void> responseTemplate = testRestTemplate.exchange(
                "/person/update/" + personId,
                HttpMethod.PUT,
                new HttpEntity<>(personRequestDto2),
                Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, responseTemplate.getStatusCode());
        Optional<PersonModel> existingPersonAfterUpdate = personService.find(personId);
        Assertions.assertTrue(existingPersonAfterUpdate.isPresent());
        Assertions.assertEquals("miguel updated", existingPersonAfterUpdate.get().getName());
    }

    @Test
    void deleteDeleteTest() {
        PersonRequestDto personRequestDto = new PersonRequestDto("miguel castro", new Date());
        PersonModel personModel = personService.save(personRequestDto.convertPersonDtoForEntity());
        String personId = personModel.getId();
        Optional<PersonModel> existingPersonBeforeDelete = personService.find(personId);
        Assertions.assertTrue(existingPersonBeforeDelete.isPresent());
        ResponseEntity<Void> responseTemplate = testRestTemplate.exchange(
                "/person/delete/" + personId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseTemplate.getStatusCode());
        Optional<PersonModel> existingPersonAfterDelete = personService.find(personId);
        Assertions.assertFalse(existingPersonAfterDelete.isPresent());
    }
}
