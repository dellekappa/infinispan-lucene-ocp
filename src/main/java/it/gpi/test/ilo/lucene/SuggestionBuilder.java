package it.gpi.test.ilo.lucene;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface SuggestionBuilder<E, P extends Serializable> {
    Suggestion<P> build(E entity);
}
