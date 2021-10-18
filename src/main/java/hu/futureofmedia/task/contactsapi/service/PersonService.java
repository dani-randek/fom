package hu.futureofmedia.task.contactsapi.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import hu.futureofmedia.task.contactsapi.entities.*;
import hu.futureofmedia.task.contactsapi.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    /**
     *  A metódus ellenőrzni, próbálja parsol-ni a kapott telefonszámot, ha ez sikerül, akkor ellenőrzi,
     *  hogy valid telefonszám-e és visszatér az eredménnyel(nem sikerült rájönni az e164 ellenőrzésre
     *  a PhoneNumberUtil-lal.)
     */
    private boolean validPhone(String phoneNumber){
        boolean validPhone = true;
        /*
        phoneNumberUtil.findNumbers(phoneNumber);
        Phonenumber.PhoneNumber tel = phoneNumberUtil.parse(p.getPhoneNumber());
        phoneNumberUtil.isValidNumber(tel);
         */
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "HU");
            return phoneNumberUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }

    /**
     *  Ellenőrzi, hogy létezik-e a kapott id-jú company és érvényes telefonszámot adtak-e meg és ha igen,
     *  akkor meghívja a mapper osztály megfelelő függvényét, majd az így kapott person-t megpróbálja
     *  elmenteni.
     */
    public ResponseEntity addPerson(PersonCreateDTO person){
        Optional<Company> company = companyService.getById(person.getCompanyId());
        if(validPhone(person.getPhoneNumber()) && company.isPresent()){
            personRepository.save(personMapper.personCreateDTOToPerson(person, company.get()));
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  Ellenőrzi, hogy létezik-e a kapott id-jú Person és Company és érvényes telefonszámot adtak-e meg és ha igen,
     *  akkor meghívja a mapper osztály megfelelő függvényét, majd az így kapott person-nek beállítja a kapott id-t
     *  majd megpróbálja elmenteni az adatbázisba.
     */
    public ResponseEntity updatePerson(Long id, PersonCreateDTO person){
        Optional<Company> company = companyService.getById(person.getCompanyId());
        Optional<Person> personToUpdate = getById(id);
        if(personToUpdate.isPresent() && company.isPresent()&& validPhone(person.getPhoneNumber())){
            Person updatedPerson = personMapper.personCreateDTOToPerson(person, company.get());
            updatedPerson.setId(id);
            personRepository.save(updatedPerson);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  Id alapján kikeres az adatbázisból egy person-t és vissza tér Optional<Person>-nel.
     */
    public Optional<Person> getById(Long id) {
        return personRepository.findById(id);
    }

    /**
     *  A getById által kapott optional-ban ellenőrzi, hogy létezik-e a keresett person, ha igen,
     *  akkor átalakítja DetailedPersonDTO-vá, majd visszatér vele.
     */
    public DetailedPersonDTO getDetailed(Long id) {
        Optional<Person> person = getById(id);
        return person.map(value -> personMapper.personToDetailedDTO(value)).orElse(null);
    }

    /**
     *  A getById által kapott optional-ban ellenőrzi, hogy létezik-e a keresett person, ha igen,
     *  akkor létrehoz egy új person-t beletölti az optional tartalmát, átállítja a status-t
     *  deleted-re, majd menti az adatbázisba.
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
