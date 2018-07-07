package cat.gencat.gsit.apis.edge.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

@EnableCaching
@Configuration
public class CacheConfiguration implements CachingConfigurer {
	Logger log = LoggerFactory.getLogger(CacheConfiguration.class);
	public static final String GICAR_USERS = "users";
	
	@Override
	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		log.info("Initializing simple Guava Cache manager.");
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		GuavaCache businessCache = new GuavaCache(GICAR_USERS,
				CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build());
		cacheManager.setCaches(Arrays.asList(businessCache));
		return cacheManager;
	}

	@Override
	public KeyGenerator keyGenerator() {
		return new SimpleKeyGenerator();
	}

	@Override
	public CacheResolver cacheResolver() {
		return new SimpleCacheResolver(cacheManager());
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return new SimpleCacheErrorHandler();
	}

}