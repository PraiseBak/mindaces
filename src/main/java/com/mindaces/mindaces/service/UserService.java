package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.Role;
import com.mindaces.mindaces.domain.entity.User;
import com.mindaces.mindaces.domain.repository.UserRepository;
import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.api.ValidCheck;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;



@Service
@AllArgsConstructor
public class UserService implements UserDetailsService
{
    private UserRepository userRepository;

    @Transactional
    public Long joinUser(UserDto userDto)
    {
        ValidCheck validCheck = new ValidCheck();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String inputID = userDto.getUserID();
        boolean isValid = validCheck.isSignupValid(inputID,userDto.getUserPassword(),userDto.getUserEmail());

        //중복확인으로 체크하긴 하지만 혹시모르니 더블 체크
        if(userRepository.findByUserID(inputID).isPresent() || !isValid || userRepository.findByUserID(userDto.getUserEmail()).isPresent())
        {
            return (long) -1;
        }

        userDto.setUserID(userDto.getUserID());
        userDto.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        userDto.setUserEmail(userDto.getUserEmail());
        return userRepository.save(userDto.toEntity()).getUserIdx();
    }
    
    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException
    {
        //Optional<User> userEntityWrapper = userRepository.findByUserEmail(userEmail);
        Optional<User> userEntityWrapper = userRepository.findByUserID(userID);
        if(userEntityWrapper.isEmpty())
        {
            System.out.println("존재하지 않은 유저입니다");
            throw new UsernameNotFoundException(userID);
        }

        User user = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();
        if(("praisebak@naver.com").equals(userID))
        {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        else
        {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserID(), user.getUserPassword(), authorities);

    }

    public Long findUserID(String userID)
    {
        Optional<User> userEntityWrapper = userRepository.findByUserID(userID);
        if(userEntityWrapper.isEmpty())
        {
            return -1L;
        }
        return 1L;
    }

    public Long findUserEmail(String userEmail)
    {
        Optional<User> userEntityWrapper = userRepository.findByUserEmail(userEmail);
        if(userEntityWrapper.isEmpty())
        {
            return -1L;
        }
        return 1L;
    }

    public void processOAuthPostLogin(String username)
    {
    }



}
