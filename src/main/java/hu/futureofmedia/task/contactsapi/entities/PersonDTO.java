package hu.futureofmedia.task.contactsapi.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {

    private String fullName;
    private String companyName;
    private String email;
    private String phoneNumber;

    public PersonDTO(Person person){
        this.fullName = person.getLastName() + person.getFirstName();
        this.companyName = person.getCompany().getName();
        this.email = person.getEmail();
        this.phoneNumber = person.getPhoneNumber();
    }

}
