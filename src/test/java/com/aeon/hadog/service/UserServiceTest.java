package com.aeon.hadog.service;

import com.aeon.hadog.base.dto.user.JoinRequestDTO;
import com.aeon.hadog.base.dto.user.LoginRequestDTO;
import com.aeon.hadog.domain.User;
import com.aeon.hadog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 테스트")
    @Transactional
    void signup() {
        // given
        JoinRequestDTO dto = JoinRequestDTO.builder()
                .name("김민지")
                .id("minji01")
                .password("minji01@")
                .nickname("김민지01")
                .email("minji01@gmail.com")
                .build();

        // when
        Long userId = userService.signup(dto);

        // then
        assertNotNull(userId);
        User findUser= userRepository.findByUserId(userId).orElseThrow();
        assertEquals(findUser.getUserId(), userId);
    }


    @Test
    @DisplayName("로그인 테스트")
    void signin() {
        // given
        String id = "user3";
        String password = "user3@@@";
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .id(id)
                .password(password)
                .build();

        // when
        String token = userService.signin(dto);

        // then
        assertNotNull(token);
    }

    @Test
    @DisplayName("아이디 중복 테스트")
    void checkId() {
        // given
        String existingId = "user3";

        // when
        boolean result = userService.checkId(existingId);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("닉네임 중복 테스트")
    void checkNickName() {
        // given
        String existingNickname = "hello4";

        // when
        boolean result = userService.checkNickName(existingNickname);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("이메일 중복 테스트")
    void checkEmail() {
        // given
        String existingEmail = "user3@gmail.com";

        // when
        boolean result = userService.checkEmail(existingEmail);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void modifyPassword() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteUser() {
        // given

        // when

        // then
    }
}