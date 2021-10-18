package hu.futureofmedia.task.contactsapi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateDTO {

    private String lastName;
    private String firstName;
    private Long companyId;
    private String email;
    private String phoneNumber;
    private String note;
}
