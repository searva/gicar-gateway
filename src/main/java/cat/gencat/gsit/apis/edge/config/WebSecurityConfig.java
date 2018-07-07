/*******************************************************************************
 * Copyright 2016 Generalitat de Catalunya.
 *
 * The contents of this file may be used under the terms of the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence"); You may not use this work except in compliance with the Licence. You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1 Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the Licence for the specific
 * language governing permissions and limitations under the Licence.
 *
 * Original authors: Centre de Suport Canigó Contact: oficina-tecnica.canigo.ctti@gencat.cat
 *******************************************************************************/
package cat.gencat.gsit.apis.edge.config;

import java.util.Collections;
import java.util.regex.Pattern;

import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import cat.gencat.gsit.apis.edge.gicar.AuthorizationUserDetailsService;




@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	  public static final String REALM_NAME = "MyRealm";
	    public static final String API_KEY_PARAM = "apikey";
	    public static final Pattern AUTHORIZATION_HEADER_PATTERN = Pattern.compile(
	        String.format("%s %s=\"(\\S+)\"", REALM_NAME, API_KEY_PARAM)
	    );

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.antMatcher("/**")
	            .addFilterAfter(preAuthenticationFilter(), RequestHeaderAuthenticationFilter.class)
	            .authorizeRequests()
	                .anyRequest().authenticated()
	            .and()
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and()
	                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
	            .and()
	                .csrf().disable();
	    }

	    @Bean
	    public RequestHeaderAuthenticationFilter preAuthenticationFilter() {
	        RequestHeaderAuthenticationFilter preAuthenticationFilter = new RequestHeaderAuthenticationFilter();
	        preAuthenticationFilter.setPrincipalRequestHeader("GICAR_ID");
	        preAuthenticationFilter.setCredentialsRequestHeader("GICAR");
	        preAuthenticationFilter.setAuthenticationManager(authenticationManager());
	        preAuthenticationFilter.setExceptionIfHeaderMissing(false);

	        return preAuthenticationFilter;
	    }

	    @Override
	    protected AuthenticationManager authenticationManager() {
	        return new ProviderManager(Collections.singletonList(authenticationProvider()));
	    }

	    @Bean
	    public AuthenticationProvider authenticationProvider() {
	        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
	        authenticationProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
	        authenticationProvider.setThrowExceptionWhenTokenRejected(false);

	        return authenticationProvider;
	    }

	    @Bean
	    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
	        return new AuthorizationUserDetailsService();
	    }

	    @Bean
	    public AuthenticationEntryPoint authenticationEntryPoint() {
	        return new Http401AuthenticationEntryPoint(REALM_NAME);
	    }

}
