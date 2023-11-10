package dk.kea.external_api_demo.service;

import dk.kea.external_api_demo.dto.AgeDTO;
import dk.kea.external_api_demo.dto.CountryDTO;
import dk.kea.external_api_demo.dto.GenderDTO;
import dk.kea.external_api_demo.entity.Person;
import dk.kea.external_api_demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person getPersonFromExternalAPIS(String name){
        Person person = new Person();
        Mono<GenderDTO> res1 = WebClient.create()
                .get()
                .uri("https://api.genderize.io/?name="+name)
                .retrieve()
                .bodyToMono(GenderDTO.class)
                .doOnError(e -> e.getMessage());
        Mono<AgeDTO> res2 = WebClient.create()
                .get()
                .uri("https://api.agify.io/?name="+name)
                .retrieve()
                .bodyToMono(AgeDTO.class)
                .doOnError(e -> e.getMessage());
        Mono<CountryDTO> res3 = WebClient.create()
                .get()
                .uri("https://api.nationalize.io/?name="+name)
                .retrieve()
                .bodyToMono(CountryDTO.class)
                .doOnError(e -> e.getMessage());

        var rs = Mono.zip(res1,res2,res3).map(tuple3 -> {
            person.setAge(tuple3.getT2().getAge());
            person.setGender(tuple3.getT1().getGender());
            person.setCountry(tuple3.getT3().getCountry().get(0).getCountry_id());
            person.setName(name);
            person.setAgeCount(tuple3.getT2().getCount());
            person.setCountryProbability(tuple3.getT3().getCountry().get(0).getProbability());
            person.setGenderProbability(tuple3.getT1().getProbability());
            return person;
        });
        rs.block();
        personRepository.save(person);
        return person;
    }

    public Person getNameInfo(String name){
        // check if name exists in database, if not, get info from external API
        Optional<Person> person = personRepository.findByName(name);
        if(person.isPresent()){
            return person.get();
        } else {
            return getPersonFromExternalAPIS(name);
        }
    }
}
