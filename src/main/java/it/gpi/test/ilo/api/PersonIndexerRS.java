package it.gpi.test.ilo.api;

import it.gpi.test.ilo.lucene.Suggester;
import it.gpi.test.ilo.lucene.Suggestion;
import it.gpi.test.ilo.lucene.SuggestionIterator;
import it.gpi.test.ilo.lucene.person.PersonSuggestionBuilder;
import it.gpi.test.ilo.pojos.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * Servizio di indicizzazione pazienti
 *
 * @author Giulio Ruggeri
 */
@RestController
@RequestMapping("/api/indexer/person")
public class PersonIndexerRS {

    private static final Set<Person> mockData = new HashSet<>();

    static {
        mockData.add(new Person("1", "BNCCLD47B12H501I", "BIANCHI", "CLAUDIO"));
        mockData.add(new Person("1", "BNCRNT65C18H501L", "BIANCHI", "RENATO"));
        mockData.add(new Person("1", "RSSMRI77D09H501Q", "ROSSI", "MARIO"));
        mockData.add(new Person("1", "VRDNDR63A28H501D", "VERDI", "ANDREA"));
    }


    @Autowired
    private Suggester personSuggester;

    @Autowired
    private PersonSuggestionBuilder personSuggestionBuilder;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity create() throws Exception {

        Set<Suggestion> suggestions = createSuggestions(mockData);
        personSuggester.build(new SuggestionIterator(suggestions.iterator()));

        return new ResponseEntity(HttpStatus.CREATED);
    }

    private Set<Suggestion> createSuggestions(Set<Person> mockData){
        Set<Suggestion> suggestions = new HashSet<>();

        mockData.forEach(x -> {
            suggestions.add(personSuggestionBuilder.build(x));
        });

        return suggestions;
    }
}
