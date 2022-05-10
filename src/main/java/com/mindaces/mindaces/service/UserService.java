package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.Role;
import com.mindaces.mindaces.domain.entity.UncheckedUser;
import com.mindaces.mindaces.domain.entity.User;
import com.mindaces.mindaces.domain.repository.UncheckedUserRepository;
import com.mindaces.mindaces.domain.repository.UserRepository;
import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.api.ValidCheck;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;



@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService
{
    private RoleService roleService;
    private UserRepository userRepository;
    private UncheckedUserRepository uncheckedUserRepository;

    //TODO 이메일 링크 << 만료시간 전에 통과되면 joinUser 조지기
    //이메일 보낼때
    //유저 정보 입력란 유효성 체크하므로 validCheck 삭제
    @Transactional
    public Long joinUser(UserDto userDto)
    {
        String inputID = userDto.getUserID();
        //중복확인으로 체크하긴 하지만 더블 체크
        if(userRepository.findByUserID(inputID) != null || userRepository.findByUserID(userDto.getUserEmail()) != null)
        {
            return (long) -1;
        }
        userDto.setUserPassword(new BCryptPasswordEncoder().encode(userDto.getUserPassword()));
        return userRepository.save(userDto.toEntity()).getUserIdx();
    }
    
    @Override
    public UserDetails loadUserByUsername(String userID) throws InternalAuthenticationServiceException
    {
        User userEntityWrapper = userRepository.findByUserID(userID);
        UserDetails userDetails;
        if(userEntityWrapper == null)
        {
            throw new UsernameNotFoundException(userID);
        }
        if(!userEntityWrapper.getIsAuthChecked())
        {
            throw new AuthenticationCredentialsNotFoundException("이메일 인증이 완료되지 않은 계정입니다\n 이메일을 확인해주세요");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        if(("praisebak@naver.com").equals(userID))
        {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        else
        {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }
        userDetails = new org.springframework.security.core.userdetails.User(userEntityWrapper.getUserID(), userEntityWrapper.getUserPassword(), authorities);
        return userDetails;
    }

    public Long findUserID(Authentication authentication)
    {
        return findUserID(roleService.getUsername(authentication));
    }

    public Long findUserID(String userID)
    {
        User userEntity;
        userEntity = userRepository.findByUserID(userID);
        if(userEntity == null)
        {
            return -1L;
        }
        return 1L;
    }


    public Long findUserEmail(String userEmail)
    {
        User userEntityWrapper = userRepository.findByUserEmail(userEmail);
        if(userEntityWrapper == null)
        {
            return -1L;
        }
        return 1L;
    }

    public Boolean isExistingEmailAndID(UserDto userDto)
    {
        String userEmail;
        String userID;
        userEmail = userDto.getUserEmail();
        userID = userDto.getUserID();
        return this.userRepository.existsByUserEmailAndUserID(userEmail,userID);
    }

    @Transactional
    public void changeAsRandomPassword(UserDto userDto, String password)
    {
        User user;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user = this.userRepository.findByUserID(userDto.getUserID());
        user.setUserPassword(passwordEncoder.encode(password));
    }

    @Transactional
    public Boolean changePassword(String originPassword, String newPasswordOne, String newPasswordTwo,String username)
    {
        User user;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ValidCheck validCheck = new ValidCheck();


        //중복 체크 (동일해야함)
        if(!newPasswordOne.equals(newPasswordTwo))
        {
            return false;
        }
        user = this.userRepository.findByUserID(username);

        //기존 패스워드와 동일한지
        if(!passwordEncoder.matches(originPassword,user.getUserPassword()))
        {
            return false;
        }

        //바꿀 패스워드가 규칙에 맞는지
        if(!validCheck.isValidPassword(newPasswordOne))
        {
            return false;
        }

        user.setUserPassword(passwordEncoder.encode(newPasswordOne));
        return true;
    }


    public void saveBeforeSignedUserInfo(String randomKey, UserDto userDto)
    {
        UncheckedUser uncheckedUser = UncheckedUser.builder()
                .key(randomKey)
                .userID(userDto.getUserID())
                .build();
        log.info(uncheckedUserRepository.save(uncheckedUser).toString());
    }

    @Transactional
    public boolean emailCheckUserSignup(String key)
    {
        Boolean result = false;
        UncheckedUser unCheckedUser = this.uncheckedUserRepository.getByKey(key);
        if(unCheckedUser != null)
        {
            try
            {
                User user = userRepository.findByUserID(unCheckedUser.getUserID());

                if(unCheckedUser.getUserID().equals(user.getUserID()))
                {
                    user.setIsAuthChecked(true);
                    //save하지않아도 transaction에서 감지
                    userRepository.save(user);
                    result = true;
                }
            }
            catch(NullPointerException e)
            {
                return false;
            }

        }
        if(result)
        {
            uncheckedUserRepository.deleteByKey(key);
        }
        return result;
    }
}
