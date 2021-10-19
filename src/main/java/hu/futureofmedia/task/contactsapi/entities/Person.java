package hu.futureofmedia.task.contactsapi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Has to be a valid e-mail address")
    private String email;

    private String phoneNumber;

    @ManyToOne
    private Company company;

    private String note;

    private Status status;


    @Column(name = "createdDate", nullable = false, updatable = false)
    private Instant createdDate;

    private Instant lastModifiedDate;

    public Person(String lastName, String firstName, Company company,  String email, String phoneNumber,
                   String note){
        this.lastName = lastName;
        this.firstName = firstName;
        this.company = company;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.note = note;
        this.status = Status.ACTIVE;
    }

    @PrePersist
    private void beforeSaving() {
        Instant i = Instant.now();
        createdDate = i;
        lastModifiedDate = i;
    }

    @PreUpdate
    private void beforeUpdating() {
        lastModifiedDate = Instant.now();
    }

}
