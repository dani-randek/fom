package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.entities.Person;
import hu.futureofmedia.task.contactsapi.entities.PersonDTO;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    /**
     *  A metódus kap egy int page paramétert, lekéri az összes ACTIVE státuszú
     *  embert az adatbázistól vezeték, majd keresztnév szerint rendezve, a kapott
     *  paraméternek megfelelő oldalon lévő elemekből csinál egy allistát, végül pedig
     *  ezeket átalakítja PersonDTO-kká és visszatér velük.
     */
    public List<PersonDTO> getAllActive(int page){
        List<Person> people = personRepository.findByStatusOrderByLastNameAscFirstNameAsc(Status.ACTIVE);
        if(page * 10 > people.size()){
            List<Person> peoplePage = people.subList((page-1)*10, people.size());
            return peoplePage.stream().map(
                    person -> new PersonDTO(person)
            ).collect(Collectors.toList());
        }
        else {
            List<Person> peoplePage = people.subList((page-1)*10, page*10);
            return peoplePage.stream().map(
                    person -> new PersonDTO(person)
            ).collect(Collectors.toList());
        }
    }
}
