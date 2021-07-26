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
        //HttpServletRequest에 따른 접근 제한해줌
        http.authorizeRequests()
                //antMathcers는 특정 경로 지정
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                        //TODO 이건 삭제해야할지도
                .antMatchers("/user/myinfo").hasAuthority("USER")
                //petmitAll -> 권한없이 통과
                .antMatchers("/**").permitAll()
                //모든 요청에 대해 인증된 사용자만 접근하도록 설정?
            .and()
                //form 기반 인증 -> HttpSession 이용
                .formLogin()
                //커스텀 로그인 폼 사용 (커스텀 로그인 foam의 action과 loginPage()의 파라미터 경로가 일치해야함 주의)
                .loginPage("/")
                //성공후 이동하는 페이지
                .defaultSuccessUrl("/")
                .failureUrl("/")
                .permitAll()
            .and()
                //http 세션 제거
                .logout()
                //커스텀 로그아웃 url로 지정
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/logout/result")
                //HTTP 세션 초기화
                .invalidateHttpSession(true)
            .and()
                //예외 발생시 이동시킬 부분
                .exceptionHandling().accessDeniedPage("/user/denied");
    }

    //AutionticationManager가 모든 인증의 주체임
    //AutionticationManagerBuilder를 이용해서 AutionticationManager를 사용함
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());

    }
}
