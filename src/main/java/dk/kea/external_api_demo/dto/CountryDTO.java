package dk.kea.external_api_demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CountryDTO {
    private int count;
    private String name;
    private List<Country> country;

    @Data
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Country {
        private String country_id;
        private double probability;
    }
}
