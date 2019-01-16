package it.gpi.test.ilo.pojos;

import java.io.Serializable;
import java.time.LocalDate;

public class PersonPayload implements Serializable {
    private String personId;
    private String fiscalCode;
    private String lastName;
    private String firstName;

    public PersonPayload() {
    }

    public PersonPayload(String personId, String fiscalCode, String lastName, String firstName) {
        this.personId = personId;
        this.fiscalCode = fiscalCode;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
