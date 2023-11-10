package dk.kea.external_api_demo.api;

import dk.kea.external_api_demo.entity.Person;
import dk.kea.external_api_demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("name-info")
    public ResponseEntity<Person> getNameInfo(@RequestParam String name) {
        return ResponseEntity.ok(personService.getNameInfo(name));
    }
}
