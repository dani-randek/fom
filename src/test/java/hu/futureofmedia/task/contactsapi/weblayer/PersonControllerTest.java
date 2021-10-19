package hu.futureofmedia.task.contactsapi.weblayer;

import hu.futureofmedia.task.contactsapi.controller.PersonController;
import hu.futureofmedia.task.contactsapi.entities.*;
import hu.futureofmedia.task.contactsapi.service.PersonMapper;
import hu.futureofmedia.task.contactsapi.service.PersonService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Megvalósítandó teszt esetek:
 * Összes listázása
 * Egy személy részletesen
 * Új személy hozzáadása
 * személy adatainak megváltoztatása
 * személy törlése/státusz változtatása
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class PersonControllerTest {

    List<PersonDTO> peopleDTOs = new ArrayList<>();
    List<DetailedPersonDTO> detailedPersonDTOS = new ArrayList<>();

    private MockMvc mockMvc;

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personService;

    @Mock
    private PersonMapper personMapper;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc= MockMvcBuilders.standaloneSetup(personController).build();

        List<Person> people = new ArrayList<>() {{
            add(new Person(1L, "Daniel", "Randek", "daniel.randek@gmail.com",
                    "+36204119494", new Company(1L, "Company #1", new ArrayList<>()), "note1", Status.ACTIVE, Instant.now(), Instant.now()));
            add(new Person(2L, "Istvan", "Kiss", "istvan.kiss@gmail.com",
                    "+36201111111", new Company(2L, "Company #2", new ArrayList<>()), "note2", Status.ACTIVE, Instant.now(), Instant.now()));
            add(new Person(3L, "Peter", "Kiss", "peter.kiss@gmail.com",
                    "+36202222222", new Company(3L, "Company #3", new ArrayList<>()), "note3", Status.ACTIVE, Instant.now(), Instant.now()));
            add(new Person(4L, "Zsolt", "Kiss", "zsolt.kiss@gmail.com",
                    "+36203333333", new Company(4L, "Company #4", new ArrayList<>()), "note4", Status.ACTIVE, Instant.now(), Instant.now()));
        }};

        peopleDTOs = people.stream().map(
                person -> personMapper.personToListDTO(person)
        ).collect(Collectors.toList());

        detailedPersonDTOS =  people.stream().map(
                person -> personMapper.personToDetailedDTO(person)
        ).collect(Collectors.toList());
    }

    @Test
    public void getDetailedPersonById() throws Exception {
        when(personService.getDetailed(1L)).thenReturn(detailedPersonDTOS.get(1));

        mockMvc.perform(MockMvcRequestBuilders.get("/people/1").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("daniel.randek@gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is("Daniel")))
        ;

        verify(personService,times(1)).getDetailed(1L);
        verifyNoMoreInteractions(personService);
    }
}
