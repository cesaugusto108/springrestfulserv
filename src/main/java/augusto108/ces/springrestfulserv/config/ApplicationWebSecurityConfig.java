package augusto108.ces.springrestfulserv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
@Profile("!test")
@PropertySource("classpath:application.properties")
public class ApplicationWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${users.username.user1}")
    private String user1;

    @Value("${users.username.user2}")
    private String user2;

    @Value("${users.username.user3}")
    private String user3;

    @Value("${users.username.user4}")
    private String user4;

    @Value("${users.roles.role1}")
    private String role1;

    @Value("${users.roles.role2}")
    private String role2;

    @Value("${users.roles.role3}")
    private String role3;

    @Value("${users.password}")
    private String password;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        User.UserBuilder users = User.withDefaultPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser(users.username(user1).password(password).roles(role1))
                .withUser(users.username(user2).password(password).roles(role1, role2))
                .withUser(users.username(user3).password(password).roles(role1))
                .withUser(users.username(user4).password(password).roles(role1, role3));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/v1/guests/**").hasRole(role1)
                .antMatchers(HttpMethod.POST, "/v1/guests/**").hasRole(role2)
                .antMatchers(HttpMethod.PUT, "/v1/guests/**").hasRole(role2)
                .antMatchers(HttpMethod.DELETE, "/v1/guests/**").hasRole(role3)
                .antMatchers(HttpMethod.PATCH, "/v1/guests/**").hasAnyRole(role2, role3)
                .antMatchers(HttpMethod.GET, "**/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET, "**/v3/api-docs/**").permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();
    }
}
