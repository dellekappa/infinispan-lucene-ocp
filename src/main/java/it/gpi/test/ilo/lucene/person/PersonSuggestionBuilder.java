package it.gpi.test.ilo.lucene.person;

import it.gpi.test.ilo.lucene.Suggestion;
import it.gpi.test.ilo.lucene.SuggestionBuilder;
import it.gpi.test.ilo.pojos.Person;
import it.gpi.test.ilo.pojos.PersonPayload;

public class PersonSuggestionBuilder implements SuggestionBuilder<Person, PersonPayload> {


    public Suggestion<PersonPayload> build(Person person) {
        if (person == null){
            return null;
        }

        String id = person.getId();
        String cf = person.getFiscalCode();
        String cogn = person.getLastName();
        String nome = person.getFirstName();

        PersonPayload payload = buildPayload(id, cf, cogn, nome);
        String text = buildText(cf, cogn, nome);

        Suggestion suggestion = new Suggestion(id, text, payload);

        return suggestion;
    }

    private PersonPayload buildPayload(String personId, String fiscalCode, String lastName, String firstName){
        return new PersonPayload(personId, fiscalCode, lastName, firstName);
    }

    private String buildText(String cf, String cogn, String nome){
        String descr = "";
        if (cf != null){
            descr += cf;
        }
        if (cogn != null){
            if (descr.length() > 0){
                descr += " ";
            }
            descr += cogn;
        }
        if (nome != null){
            if (descr.length() > 0){
                descr += " ";
            }
            descr += nome;
        }

        return descr;
    }
}
