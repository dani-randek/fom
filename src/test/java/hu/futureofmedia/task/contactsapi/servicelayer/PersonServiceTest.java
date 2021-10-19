package hu.futureofmedia.task.contactsapi.servicelayer;


import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Person;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.repositories.PersonRepository;
import hu.futureofmedia.task.contactsapi.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PersonServiceTest {

    @TestConfiguration
    static class PersonServiceImplTestContextConfiguration {

        @Bean
        public PersonService personService() {
            return new PersonService();
        }
    }

    List<Person> personList = new ArrayList<>();


    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        Pageable sortedByLastNameAscFirstNameAsc  = PageRequest.of(0, 10, Sort.by("lastName").and(Sort.by("firstName")));

        Company company = new Company(1L, "Company #1", personList);
        Instant i = Instant.now();
        Person person = new Person(1L, "Daniel", "Randek", "daniel.randek@gmail.com",
                "+36204119494", company, "note", Status.ACTIVE, i, i);
        personList.add(person);

        Mockito.when(personRepository.findById(person.getId()))
                .thenReturn(java.util.Optional.of(person));

        Mockito.when(personRepository.findAll())
                .thenReturn(personList);

        Mockito.when(personRepository.findByStatus(Status.ACTIVE, sortedByLastNameAscFirstNameAsc))
                .thenReturn(personList);
    }

    @Test
    public void personGetAllActive() {
        assertThat(personService.getAllActive(1).size())
                .isEqualTo(1);

        Instant i = Instant.now();
        Person person = new Person(2L, "Kiss", "Tamas", "tamas.kiss@gmail.com",
                "+36301111111", new Company(2L, "Company #2", new ArrayList<>()), "note", Status.ACTIVE, i, i);
        personList.add(person);

        assertThat(personService.getAllActive(0).size())
                .isEqualTo(2);
    }
}
