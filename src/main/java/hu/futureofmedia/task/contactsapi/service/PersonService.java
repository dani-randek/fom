package hu.futureofmedia.task.contactsapi.service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import hu.futureofmedia.task.contactsapi.entities.*;
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

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Autowired
    CompanyService companyService;

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

    private boolean validPhone(String phoneNumber){
        boolean validPhone = true;
        /*
        phoneNumberUtil.findNumbers(phoneNumber);
        Phonenumber.PhoneNumber tel = phoneNumberUtil.parse(p.getPhoneNumber());
        phoneNumberUtil.isValidNumber(tel);
         */
        if(validPhone){
            return true;
        }
        return false;
    }

    /**
     *  Elment az adatbázisba egy a controllertől kapott person-t.
     */
    public void addPerson(PersonCreateDTO person){
        Optional<Company> company = companyService.getById(person.getCompanyId());
        if(validPhone(person.getPhoneNumber()) && company.isPresent()){
            personRepository.save(personMapper.personCreateDTOToPerson(person, company.get()));
        }
    }

    public void updatePerson(Long id, PersonCreateDTO person){
        Optional<Company> company = companyService.getById(person.getCompanyId());
        Optional<Person> personToUpdate = getById(id);
        if(personToUpdate.isPresent() && company.isPresent()&& validPhone(person.getPhoneNumber())){
            Person updatedPerson = personMapper.personCreateDTOToPerson(person, company.get());
            updatedPerson.setId(id);
            personRepository.save(updatedPerson);
        }
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
        return person.map(value -> personMapper.personToDetailedDTO(value)).orElse(null);
    }

    /**
     *  Kikeres egy person-t, megnézi létezik-e, ha igen akkor törli.
     */
    public void deleteById(Long id) {
        Optional<Person> personById = getById(id);
        Person person = new Person();
        if(personById.isPresent())
            person = personById.get();
            person.setStatus(Status.DELETED);
            personRepository.save(person);
    }
}
