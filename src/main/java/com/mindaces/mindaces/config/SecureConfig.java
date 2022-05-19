package com.mindaces.mindaces.config;

import com.mindaces.mindaces.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;


//설정관련 클래스는 Configuration이라고 명시해줘야함
@Configuration
//WebSecurity에 대한 클래스라는 것 명시
//(Configuration이 합쳐졌으니 WebSecurity에 대한 설정하는 클래스)
@EnableWebSecurity
@AllArgsConstructor
//WebSecurityConfigurerAdapter를 상속받아 일반적으로 메서드를 구현하는게 일반적인 방법
public class
SecureConfig extends WebSecurityConfigurerAdapter
{
    private final UserService userService;
    private final CustomAuthenticationFailHandler customAuthenticationFailHandler;
    private final DataSource dataSource;

    //스프링 제공 비밀번호 암호화 객체
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    //resources/static 이후의 파일들을 통과시킴
    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }
    //HTTP 요청에 대한 보안 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
                // 페이지 권한 설정
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/admin/**/**").hasRole("ADMIN")
                .antMatchers("/user/userObjPage").authenticated()
                .antMatchers("/user/myinfo").authenticated()
                .antMatchers("/**").permitAll()
                .and() // 로그인 설정
                .formLogin()
                .loginPage("/user/login")
                .failureHandler(customAuthenticationFailHandler)
                .defaultSuccessUrl("/")
                .permitAll()
                .and() // 로그아웃 설정
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                // 403 예외처리 핸들링
                //this line has effect when user is authenticated
                .exceptionHandling()
                .accessDeniedPage("/error/noPermission");

        http.csrf()
                .ignoringAntMatchers("/API/**")
                .ignoringAntMatchers("/gallery/**/**")
                .ignoringAntMatchers("/gallery/**/modify/**")
                .ignoringAntMatchers("/gallery/**/delete/**")
                .ignoringAntMatchers("/gallery/**/postWrite/**")
                .ignoringAntMatchers("/user/userObjAdd");

        http.rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(3600)
                .userDetailsService(userService)
                .tokenRepository(tokenRepository());


        http.oauth2Login()
                .loginPage("/user/login");
    }

    //AutionticationManager가 모든 인증의 주체임
    //AutionticationManagerBuilder를 이용해서 AutionticationManager를 사용함
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        //220405 22:48
        //아래의 코드가 인증을 myAuthProvider을 대체하고 있었음
        //무엇을 위해 이 코드를 작성했는지 모르겠으나 내 의도에는 맞지않아 주석처리함
        //auth.authenticationProvider(myAuthProvider);
        //무엇을 위해 작성했는지 모르겠다는 것은 좀 문제가 있는 사항인 것 같음
        //기록. 알기위해서는 기록하는 습관을 들일 것
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        // JDBC 기반의 tokenRepository 구현체
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // dataSource 주입
        return jdbcTokenRepository;
    }

}
