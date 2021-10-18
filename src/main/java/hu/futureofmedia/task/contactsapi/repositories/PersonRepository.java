package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.Person;
import hu.futureofmedia.task.contactsapi.entities.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository  extends CrudRepository<Person, Long> {
    List<Person> findAll();
    List<Person> findByStatusOrderByLastNameAscFirstNameAsc(Status status);
}
