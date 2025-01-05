package uv.uberEats;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import uv.uberEats.repositories.UsuarioRepository;

@Configuration
public class ApplicationConfiguration {

    private final UsuarioRepository usuarioRepository;

    public ApplicationConfiguration(UsuarioRepository usuarioRepository) {this.usuarioRepository = usuarioRepository;}

    @Bean
    UserDetailsService userDetailsService() {
        return username -> usuarioRepository.validateUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    Md4PasswordEncoder passwordEncoder() {
        return new  Md4PasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /*@Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return server -> server.addAdditionalTomcatConnectors(createStandardConnector());
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(8081);
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }*/
}
