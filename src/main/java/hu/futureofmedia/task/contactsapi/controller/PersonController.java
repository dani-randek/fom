package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.Person;
import hu.futureofmedia.task.contactsapi.entities.PersonDTO;
import hu.futureofmedia.task.contactsapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/list/{page}")
    public List<PersonDTO> getAllActivePeople(@PathVariable("page") int page){
        return personService.getAllActive(page);
    }

}
