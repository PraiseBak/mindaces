package com.mindaces.mindaces.config;

import com.mindaces.mindaces.service.CustomOAuth2UserService;
import com.mindaces.mindaces.service.UserService;
import com.mindaces.mindaces.service.social.CustomOAuth2User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
    private CustomOAuth2UserService oauthUserService;
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
        //HttpServletRequest에 따른 접근 제한해줌
        http.authorizeRequests()
                //antMathcers는 특정 경로 지정
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                        //TODO 이건 삭제해야할지도
                .antMatchers("/user/myinfo").hasAuthority("USER")
                //petmitAll -> 권한없이 통과
                .antMatchers("/**").permitAll()
                .antMatchers("/","user/login","/auth/**","/auth/google/callback/**").permitAll()
                .anyRequest().authenticated()
            .and()
                //form 기반 인증 -> HttpSession 이용
                .formLogin().permitAll()
                //커스텀 로그인 폼 사용 (커스텀 로그인 foam의 action과 loginPage()의 파라미터 경로가 일치해야함 주의)
                .loginPage("/user/login")
                //성공후 이동하는 페이지
                .defaultSuccessUrl("/")
                .failureUrl("/fail")
                .permitAll()
            .and()
                //http 세션 제거
                .logout()
                //커스텀 로그아웃 url로 지정
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                //.logoutUrl("/user/logout")
                .logoutSuccessUrl("/user/logout/result")
                //HTTP 세션 초기화
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID").permitAll().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
                //예외 발생시 이동시킬 부분
                .exceptionHandling().accessDeniedPage("/user/denied")
            .and()
                .oauth2Login().loginPage("/user/login")
                .userInfoEndpoint()
                    .userService(oauthUserService)

            .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException
                    {
                        System.out.println("테스트");
                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                        userService.processOAuthPostLogin(oauthUser.getEmail());
                        response.sendRedirect("/");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler()
                {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
                    {
                        System.out.println("인증에 실패했다고 생각하는건가 아니면 애초에 url이 인식이 안되는걸까");
                    }
                })
        .and()
            .oauth2Login().authorizationEndpoint().baseUri("/auth/google/callback");



            http.csrf()
                .ignoringAntMatchers("/sendIDAPI");



    }



    //AutionticationManager가 모든 인증의 주체임
    //AutionticationManagerBuilder를 이용해서 AutionticationManager를 사용함
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());

    }

}



