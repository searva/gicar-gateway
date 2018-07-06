package cat.gencat.gsit.apis.edge;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;

import cat.gencat.gsit.apis.edge.oauth.TokenPreFilter;

@SpringBootApplication
@EnableZuulProxy
@EnableAutoConfiguration
public class PublicGatewayApplication extends SpringBootServletInitializer implements WebApplicationInitializer{
	private static final String ENTORN = "entorn";
	@Bean
	public TokenPreFilter tokenFilter() {
		return new TokenPreFilter();
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PublicGatewayApplication.class);
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(PublicGatewayApplication.class, args);
	}
	
	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		String entorn = System.getProperty(ENTORN);
		if (entorn == null) {
			entorn = "loc";
		}
		servletContext.setInitParameter("spring.profiles.active", entorn);
		super.onStartup(servletContext);
	}

}
