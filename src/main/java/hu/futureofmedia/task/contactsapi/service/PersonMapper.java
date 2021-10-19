package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.entities.*;
import org.springframework.stereotype.Service;

@Service
public class PersonMapper {

    public PersonDTO personToListDTO(Person person){
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setFullName(person.getLastName() +" " +person.getFirstName());
        personDTO.setCompanyName(person.getCompany().getName());
        personDTO.setEmail(person.getEmail());
        personDTO.setPhoneNumber(person.getPhoneNumber());
        return personDTO;
    }

    public DetailedPersonDTO personToDetailedDTO(Person person){
        DetailedPersonDTO personDTO = new DetailedPersonDTO();
        personDTO.setId(person.getId());
        personDTO.setLastName(person.getLastName());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setCompanyName(person.getCompany().getName());
        personDTO.setEmail(person.getEmail());
        personDTO.setPhoneNumber(person.getPhoneNumber());
        personDTO.setNote(person.getNote());
        personDTO.setCreatedDate(person.getCreatedDate());
        personDTO.setLastModifiedDate(person.getLastModifiedDate());
        return personDTO;
    }

    public Person personCreateDTOToPerson(PersonCreateDTO p, Company company){
            return new Person(p.getLastName(),
                    p.getFirstName(),
                    company,
                    p.getEmail(),
                    p.getPhoneNumber(),
                    p.getNote());
    }
}
