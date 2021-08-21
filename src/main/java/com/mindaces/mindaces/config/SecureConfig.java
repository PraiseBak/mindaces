package com.mindaces.mindaces.config;

import com.mindaces.mindaces.service.UserService;
import com.mindaces.mindaces.service.social.GoogleOAuth2User;
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
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


//설정관련 클래스는 Configuration이라고 명시해줘야함
@Configuration
//WebSecurity에 대한 클래스라는 것 명시
//(Configuration이 합쳐졌으니 WebSecurity에 대한 설정하는 클래스)
@EnableWebSecurity
@AllArgsConstructor
//WebSecurityConfigurerAdapter를 상속받아 일반적으로 메서드를 구현하는게 일반적인 방법
public class SecureConfig extends WebSecurityConfigurerAdapter
{

    private UserService userService;

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
                .antMatchers("/user/myinfo").hasRole("USER")
                .antMatchers("/**").permitAll()
                .and() // 로그인 설정
                    .formLogin()
                    .loginPage("/user/login")
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
                        .accessDeniedPage("/user/denied");

        http.csrf()
                            .ignoringAntMatchers("/API/**")
                            .ignoringAntMatchers("/gallery/**/modify/**")
                            .ignoringAntMatchers("/gallery/**/delete/**")
                            .ignoringAntMatchers("/gallery/**/postWrite/**");


                /*
                .ignoringAntMatchers("/sendIDAPI")
                .ignoringAntMatchers("/checkBoardPasswordAPI")
                        .ignoringAntMatchers(/)

                 */

        http.oauth2Login()
                .loginPage("/user/login");
    }





    //AutionticationManager가 모든 인증의 주체임
    //AutionticationManagerBuilder를 이용해서 AutionticationManager를 사용함
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

}
