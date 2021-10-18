package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.Person;
import hu.futureofmedia.task.contactsapi.entities.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository  extends JpaRepository<Person, Long> {
    List<Person> findAll();
    List<Person> findByStatus(Status status, Pageable pageable);
}
