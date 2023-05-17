package com.github.mpacala00.forum.security.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import com.github.mpacala00.forum.security.NoRedirectStrategy;
import com.github.mpacala00.forum.security.token.TokenAuthenticationFilter;
import com.github.mpacala00.forum.security.token.TokenAuthenticationProvider;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //allows to user annotations to secure methods, @Secured etc.
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     * URLs starting with /public/** are excluded from security, which means any url starting with /public will not be secured,
     * The TokenAuthenticationFilter is registered within the Spring Security Filter Chain very early. We want it to catch any authentication token passing by,
     * Most other login methods like formLogin or httpBasic have been disabled as we’re not willing to use them here (we want to use our own system),
     * Some boiler-plate code to disable automatic filter registration, related to Spring Boot.
     */

    static RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/public/**")
            //add more here after comma
    );

    static RequestMatcher PUBLIC_USER_URLS = new AntPathRequestMatcher("/public/user/**");

    static RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    TokenAuthenticationProvider provider;

    public SecurityConfig(TokenAuthenticationProvider provider) {
        super();
        this.provider = provider;
    }

    //use the custom provider
    @Override
    public void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(final WebSecurity security) {
        security.ignoring().requestMatchers(PUBLIC_USER_URLS);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                // this entry point handles when you request a protected page and you are not yet
                // authenticated
                .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
                .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PUBLIC_URLS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(PUBLIC_URLS).authenticated() //todo find a way to retrieve tokens from public endpoints
                .requestMatchers(PROTECTED_URLS).authenticated()
                .requestMatchers(PUBLIC_USER_URLS).permitAll()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(new OrRequestMatcher(PUBLIC_URLS, PROTECTED_URLS));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

    @Bean
    FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
        final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}

