package it.gpi.test.ilo.lucene;

import org.apache.lucene.util.BytesRef;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public class Suggestion<T extends Serializable> {
    private String id;
    private String text;
    private T payload;
    private Set<String> contexts;
    private long weight;
    private BytesRef brId;
    private BytesRef brText;
    private BytesRef brPayload;
    private Set<BytesRef> brContexts;

    public Suggestion(String id, String text, T payload) {
        this(id, text, payload, null, 1l);
    }

    public Suggestion(String id, String text, T payload, Set<String> contexts, long weight) {
        this.id = id;
        this.text = text;
        this.payload = payload;
        this.contexts = contexts;
        this.weight = weight;
        this.brId = new BytesRef(id);
        this.brText = new BytesRef(text);
        if (payload != null){
            if (payload instanceof CharSequence){
                this.brPayload = new BytesRef((CharSequence)payload);
            }else{
                this.brPayload = LuceneUtils.objectToBytesRef(payload);
            }
        }
        if (contexts != null){
            brContexts = contexts.stream()
                    .map(s -> new BytesRef(s))
                    .collect(Collectors.toSet());
        }

    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public T getPayload() {
        return payload;
    }

    public Set<String> getContexts() {
        return contexts;
    }

    public long getWeight() {
        return weight;
    }

    public BytesRef getBrId() {
        return brId;
    }

    public BytesRef getBrText() {
        return brText;
    }

    public BytesRef getBrPayload() {
        return brPayload;
    }

    public Set<BytesRef> getBrContexts() {
        return brContexts;
    }
}
