package property.app.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.authorizeRequests()
                    .requestMatchers(EndpointRequest.to("info")).permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/").permitAll()
                .antMatchers("/adds/submit").hasRole("USER")
                .antMatchers("/profile").hasRole("USER")
                    .antMatchers("/h2/**").permitAll()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .usernameParameter("email")
//                    .and()
//                .oauth2Login()
//                     .loginPage("/login")
//                     .userInfoEndpoint().userService(oAuth2UserService)
//                     .and()
                .and()
                .logout()
                    .and()
                .rememberMe()
                .and()
                    .csrf().disable()
                    .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
  
}
