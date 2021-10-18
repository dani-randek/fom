package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.DetailedPersonDTO;
import hu.futureofmedia.task.contactsapi.entities.Person;
import hu.futureofmedia.task.contactsapi.entities.PersonCreateDTO;
import hu.futureofmedia.task.contactsapi.entities.PersonDTO;
import hu.futureofmedia.task.contactsapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/list/{page}")
    public List<PersonDTO> getAllActivePeople(@PathVariable("page") int page){
        return personService.getAllActive(page);
    }

    @GetMapping(path = "{id}")
    public DetailedPersonDTO getDetailedById(@PathVariable("id") Long id){
        return personService.getDetailed(id);
    }

    @PostMapping("/create")
    public void addPerson(@RequestBody PersonCreateDTO person){
        personService.addPerson(person);
    }

    @PutMapping(path = "{id}")
    public void updatePersonById(@PathVariable("id") Long id, @RequestBody PersonCreateDTO person){
        personService.updatePerson(id, person);
    }

    @DeleteMapping(path = "{id}")
    public void deletePersonById(@PathVariable("id") Long id){
        personService.deleteById(id);
    }

}
