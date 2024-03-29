package hu.futureofmedia.task.contactsapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonDTO {

    private Long id;
    private String fullName;
    private String companyName;
    private String email;
    private String phoneNumber;

}
