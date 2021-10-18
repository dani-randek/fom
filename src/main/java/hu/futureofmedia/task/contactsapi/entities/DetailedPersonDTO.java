package hu.futureofmedia.task.contactsapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class DetailedPersonDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String companyName;
    private String email;
    private String phoneNumber;
    private String note;
    private Instant createdDate;
    private Instant lastModifiedDate;

}
