package it.gpi.test.ilo.lucene;

import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Giulio Ruggeri
 */
public class SuggestionIterator implements IdentifiableInputIterator {
    private final Iterator<Suggestion> iterator;
    private Suggestion current;

    public SuggestionIterator(Iterator<Suggestion> it) {
        this.iterator = it;
    }

    @Override
    public long weight() {
        return current.getWeight();
    }

    @Override
    public BytesRef id() {
        return current.getBrId();
    }

    @Override
    public BytesRef payload() {
        return current.getBrPayload();
    }

    @Override
    public boolean hasPayloads() {
        return true;
    }

    @Override
    public Set<BytesRef> contexts() {
        return current.getBrContexts();
    }

    @Override
    public boolean hasContexts() {
        return false;
    }

    @Override
    public BytesRef next() throws IOException {
        if (!iterator.hasNext()){
            return null;
        }

        current = iterator.next();

        return current.getBrText();
    }

}