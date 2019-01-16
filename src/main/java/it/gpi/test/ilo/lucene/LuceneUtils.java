package it.gpi.test.ilo.lucene;

import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.infinispan.lucene.directory.DirectoryBuilder;
import org.infinispan.manager.EmbeddedCacheManager;

import java.io.*;

public class LuceneUtils {
    public static BytesRef objectToBytesRef(Object obj){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.close();
            return new BytesRef(bos.toByteArray());
        } catch (IOException e) {
            throw new Error("Well that's unfortunate.");
        }
    }

    public static <T> T extractPayload(Lookup.LookupResult result) throws IOException, ClassNotFoundException {
        if (result == null){
            return null;
        }
        T payload = null;
        BytesRef brPayload = result.payload;
        if (brPayload != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(brPayload.bytes);
            ObjectInputStream in = new ObjectInputStream(bis);
            payload = (T) in.readObject();
        }

        return payload;
    }

    public static Directory openDirectory(String indexName, EmbeddedCacheManager infinispanCacheManager) {
        Directory directory = DirectoryBuilder.newDirectoryInstance(
                    infinispanCacheManager.getCache("lucene-metadata"),
                    infinispanCacheManager.getCache("lucene-data"),
                    infinispanCacheManager.getCache("lucene-lock"),
                    indexName).create();

        return directory;
    }
}
