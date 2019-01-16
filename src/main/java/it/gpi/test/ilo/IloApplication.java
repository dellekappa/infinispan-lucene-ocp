package it.gpi.test.ilo;

import it.gpi.test.ilo.lucene.LuceneUtils;
import it.gpi.test.ilo.lucene.Suggester;
import it.gpi.test.ilo.lucene.person.PersonSuggestionBuilder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.store.Directory;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionType;
import org.infinispan.lucene.LuceneKey2StringMapper;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;
import org.infinispan.transaction.TransactionMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class IloApplication {
	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	@Value("${spring.datasource.username}")
	private String datasourceUsername;
	@Value("${spring.datasource.password}")
	private String datasourcePassword;
	@Value("${spring.datasource.driver-class-name:}")
	private String datasourceDriverClassName;

	public static void main(String[] args) {
		SpringApplication.run(IloApplication.class, args);
	}

	@Bean
	public EmbeddedCacheManager infinispanCacheManager() {
		GlobalConfigurationBuilder gcb = GlobalConfigurationBuilder.defaultClusteredBuilder();

		EmbeddedCacheManager defaultCacheManager = new DefaultCacheManager(gcb.build());

		configureLuceneMetadataCache(defaultCacheManager);
		configureLuceneLockCache(defaultCacheManager);
		configureLuceneDataCache(defaultCacheManager);

        return defaultCacheManager;
	}

	private void configureLuceneMetadataCache(EmbeddedCacheManager defaultCacheManager){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.clustering().cacheMode(CacheMode.REPL_SYNC)
				.remoteTimeout(25000)
			.transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL)
			.indexing().index(Index.NONE)
			.persistence()
				.passivation(false)
				.addStore(JdbcStringBasedStoreConfigurationBuilder.class).preload(true)
					.fetchPersistentState(false)
					.ignoreModifications(false)
					.purgeOnStartup(false)
					.shared(true)
					.key2StringMapper(LuceneKey2StringMapper.class)
					.table()
						.dropOnExit(false)
						.createOnStart(true)
						.tableNamePrefix("ISPN")
						.idColumnName("`KEY`").idColumnType("VARCHAR(255)")
						.dataColumnName("`DATA`").dataColumnType("BLOB")
						.timestampColumnName("`VERSION`").timestampColumnType("BIGINT(20)")
					.connectionPool()
						.connectionUrl(datasourceUrl)
						.username(datasourceUsername)
						.password(datasourcePassword)
						.driverClass(datasourceDriverClassName);
					/*.dataSource()
						.jndiUrl(datasourceProperties().getJndiName());*/
		defaultCacheManager.defineConfiguration("lucene-metadata", cb.build());
	}


	private void configureLuceneLockCache(EmbeddedCacheManager defaultCacheManager){
        ConfigurationBuilder cb = new ConfigurationBuilder();
		cb
			.clustering().cacheMode(CacheMode.REPL_SYNC)
                .remoteTimeout(25000)
            .transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL)
            .indexing().index(Index.NONE);
		defaultCacheManager.defineConfiguration("lucene-lock", cb.build());
	}

	private void configureLuceneDataCache(EmbeddedCacheManager defaultCacheManager){
        ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.clustering().cacheMode(CacheMode.REPL_SYNC)
				.remoteTimeout(25000)
			.transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL)
            .memory()
                .storageType(StorageType.OBJECT)
                .evictionType(EvictionType.COUNT)
			.eviction()
				.strategy(EvictionStrategy.REMOVE)
				.size(250)
			.expiration().maxIdle(-1l)
			.indexing().index(Index.NONE)
			.persistence()
				.passivation(false)
				.addStore(JdbcStringBasedStoreConfigurationBuilder.class)
					.preload(false)
					.fetchPersistentState(false)
					.ignoreModifications(false)
					.purgeOnStartup(false)
					.shared(true)
					.key2StringMapper(LuceneKey2StringMapper.class)
					.table()
						.dropOnExit(false)
						.createOnStart(true)
						.tableNamePrefix("ISPN")
				.idColumnName("`KEY`").idColumnType("VARCHAR(255)")
				.dataColumnName("`DATA`").dataColumnType("BLOB")
				.timestampColumnName("`VERSION`").timestampColumnType("BIGINT(20)")
					.connectionPool()
						.connectionUrl(datasourceUrl)
						.username(datasourceUsername)
						.password(datasourcePassword)
						.driverClass(datasourceDriverClassName);
					/*.dataSource()
						.jndiUrl(datasourceProperties().getJndiName());*/
		defaultCacheManager.defineConfiguration("lucene-data", cb.build());
	}

	@Bean
	public Directory personSuggesterDirectory() throws IOException {
		return LuceneUtils.openDirectory("persons", infinispanCacheManager());
	}

	@Bean(destroyMethod = "close")
	public Suggester personSuggester() throws IOException {

		Analyzer analyzer = CustomAnalyzer.builder()
				.withTokenizer(StandardTokenizerFactory.class)
				.addTokenFilter(StandardFilterFactory.class)
				.addTokenFilter(LowerCaseFilterFactory.class)
				.addTokenFilter(ASCIIFoldingFilterFactory.class, "preserveOriginal", "true")
				.build();

		Suggester suggester = new Suggester(personSuggesterDirectory(),
				analyzer, analyzer,
				3, false, false, true, true);
		return suggester;
	}

	@Bean
	public PersonSuggestionBuilder personSuggestionBuilder() throws IOException {
		return new PersonSuggestionBuilder();
	}
}
