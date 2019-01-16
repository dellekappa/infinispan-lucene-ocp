package it.gpi.test.ilo.api;

import it.gpi.test.ilo.lucene.LuceneUtils;
import it.gpi.test.ilo.lucene.Suggester;
import it.gpi.test.ilo.pojos.Person;
import it.gpi.test.ilo.pojos.PersonPayload;
import org.apache.lucene.search.suggest.Lookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonRS {

    @Autowired
    private Suggester personSuggester;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Person> find(@RequestParam(name = "q", required = false) String pattern) throws Exception {

        List<Person> results = new ArrayList<>();

        if (pattern == null){
            pattern = "";
        }


        List<Lookup.LookupResult> matches;
        matches = personSuggester.lookup(pattern, 100, true, false);

        for (Lookup.LookupResult match : matches) {
            Person p = getPayload(match);
            results.add(p);
        }

        return results;
    }


    private static Person getPayload(Lookup.LookupResult result)
    {
        try {
            PersonPayload payload = LuceneUtils.extractPayload(result);
            if (payload != null){
                Person p = new Person();
                p.setId(payload.getPersonId());
                p.setFirstName(payload.getFirstName());
                p.setLastName(payload.getLastName());
                p.setFiscalCode(payload.getFiscalCode());

                return p;
            } else {
                return null;
            }
        } catch (IOException |ClassNotFoundException e) {
            throw new Error("Could not decode payload :(");
        }
    }
}
