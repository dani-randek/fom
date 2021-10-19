package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.DetailedPersonDTO;
import hu.futureofmedia.task.contactsapi.entities.PersonCreateDTO;
import hu.futureofmedia.task.contactsapi.entities.PersonDTO;
import hu.futureofmedia.task.contactsapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonService personService;

    /**
     *  A /people/list/{page} végpontra érkező get kérésekre adja vissza a megfelő oldalon lévő embereket.
     */
    @GetMapping("/list/{page}")
    public List<PersonDTO> getAllActivePeople(@PathVariable("page") int page){
        return personService.getAllActive(page);
    }

    /**
     *  A /people/{id} végpontra érkező get kérésekre adja vissza az adott embert, ha létezik.
     */
    @GetMapping(path = "{id}")
    public DetailedPersonDTO getDetailedById(@PathVariable("id") Long id){
        return personService.getDetailed(id);
    }

    /**
     *  A /people/create végpontra érkező post kérésekre felvesz egy új személyt az adatbázisba, ha a requestbody-ban
     *  érkező adatok megfelelőek.
     */
    @PostMapping("/create")
    public ResponseEntity addPerson(@RequestBody PersonCreateDTO person){
        return personService.addPerson(person);
    }

    /**
     *  A /people/{id} végpontra érkező put kérésekre megváltoztatja az adott ember adatait, ha létezik és
     *  az adatok megfelelőek.
     */
    @PutMapping(path = "{id}")
    public ResponseEntity updatePersonById(@PathVariable("id") Long id, @RequestBody PersonCreateDTO person){
        return personService.updatePerson(id, person);
    }

    /**
     *  A /people/{id} végpontra érkező delete kérésekre megváltoztatja az adott ember státuszát aktívról
     *   töröltre, ha létezik az adott személy.
     */
    @DeleteMapping(path = "{id}")
    public void deletePersonById(@PathVariable("id") Long id){
        personService.deleteById(id);
    }

}
