package hu.futureofmedia.task.contactsapi.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
public class Company {
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "company")
    private List<Person> people;
}
