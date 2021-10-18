package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.entities.DetailedPersonDTO;
import hu.futureofmedia.task.contactsapi.entities.Person;
import hu.futureofmedia.task.contactsapi.entities.PersonDTO;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonMapper personMapper;

    /**
     *  A metódus csinál egy pageable-t 10-es oldalmérettel, a requestből kapott oldalszámmal, a vezetéknév, majd a
     *  keresztnév szerint növekvő sorrendbe rendezve, ezt továbbadja a repositorynak, ami kikeresi a megfelelő oldalon lévő
     *  aktív person-eket végül pedig átalakítja ezt a listát a PersonMapper osztály segítségével PersonDTO listává és ezzel
     *  tér vissza.
     */
    public List<PersonDTO> getAllActive(int page){
        Pageable sortedByLastNameAscFirstNameAsc  = PageRequest.of(page, 10, Sort.by("lastName").and(Sort.by("firstName")));
        List<Person> people = personRepository.findByStatus(Status.ACTIVE, sortedByLastNameAscFirstNameAsc);
        return people.stream().map(
                person -> personMapper.personToListDTO(person)
        ).collect(Collectors.toList());
    }


    /**
     *  Elment az adatbázisba egy a controllertől kapott person-t.
     */
    public void addPerson(Person person){
        personRepository.save(person);
    }

    /**
     *  Id alapján kikeres az adatbázisból egy person-t és vissza tér Optional<Person>-nel.
     */
    public Optional<Person> getById(Long id) {
        return personRepository.findById(id);
    }

    /**
     *  Kikeres egy person-t, megnézi létezik-e, ha igen, akkor átalakítja DetailedPersonDTO-vá, majd visszatér vele.
     */
    public DetailedPersonDTO getDetailed(Long id) {
        Optional<Person> person = getById(id);
        if(person.isPresent()){
            return personMapper.personToDetailedDTO(person.get());
        }
        else return null;
    }

    /**
     *  Kikeres egy person-t, megnézi létezik-e, ha igen akkor törli.
     */
    public void deleteById(Long id) {
        if(getById(id).isPresent())
            personRepository.deleteById(id);
    }
}
