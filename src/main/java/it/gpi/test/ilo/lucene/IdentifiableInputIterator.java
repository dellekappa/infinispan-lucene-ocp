package it.gpi.test.ilo.lucene;

import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.util.BytesRef;

public interface IdentifiableInputIterator extends InputIterator {
    BytesRef id();
}
