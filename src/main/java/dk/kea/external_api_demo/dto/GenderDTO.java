package dk.kea.external_api_demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GenderDTO {
    String gender;
    String name;
    int count;
    double probability;
}
