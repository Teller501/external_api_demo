package dk.kea.external_api_demo.service;

import dk.kea.external_api_demo.dto.Age;
import dk.kea.external_api_demo.dto.Country;
import dk.kea.external_api_demo.dto.Gender;
import dk.kea.external_api_demo.dto.Person;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PersonService {
    public Person getPersonFromExternalAPIS(String name){
        Person person = new Person();
        Mono<Gender> res1 = WebClient.create()
                .get()
                .uri("https://api.genderize.io/?name="+name)
                .retrieve()
                .bodyToMono(Gender.class)
                .doOnError(e -> e.getMessage());
        Mono<Age> res2 = WebClient.create()
                .get()
                .uri("https://api.agify.io/?name="+name)
                .retrieve()
                .bodyToMono(Age.class)
                .doOnError(e -> e.getMessage());
        Mono<Country> res3 = WebClient.create()
                .get()
                .uri("https://api.nationalize.io/?name="+name)
                .retrieve()
                .bodyToMono(Country.class)
                .doOnError(e -> e.getMessage());

        var rs = Mono.zip(res1,res2,res3).map(tuple3 -> {
            person.setAge(tuple3.getT2().getAge());
            person.setGender(tuple3.getT1().getGender());
            person.setCountry(tuple3.getT3().getCountry_id());
            person.setName(name);
            person.setAgeCount(tuple3.getT2().getCount());
            person.setCountryProbability(tuple3.getT3().getProbability());
            person.setGenderProbability(tuple3.getT1().getProbability());
            return person;
        });
        rs.block();
        return person;
    }
}
