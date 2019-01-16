package it.gpi.test.ilo.pojos;

public class Person {
    private String id;
    private String fiscalCode;
    private String lastName;
    private String firstName;

    public Person() {
    }

    public Person(String id, String fiscalCode, String lastName, String firstName) {
        this.id = id;
        this.fiscalCode = fiscalCode;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return getFiscalCode() + " " + getLastName() + " " + getFirstName();
    }
}
