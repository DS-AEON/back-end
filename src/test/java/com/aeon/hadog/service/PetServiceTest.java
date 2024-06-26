package com.aeon.hadog.service;

import com.aeon.hadog.base.dto.pet.PetDTO;
import com.aeon.hadog.base.dto.user.JoinRequestDTO;
import com.aeon.hadog.base.dto.user.LoginRequestDTO;
import com.aeon.hadog.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class PetServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    public void before() {
        // 회원가입
        JoinRequestDTO user = new JoinRequestDTO();
        user.setId("user");
        user.setName("홍길동");
        user.setNickname("유저");
        user.setPassword("Pw1234!!");
        user.setEmail("user123@gmail.com");
        userService.signup(user);
        // 로그인
        LoginRequestDTO loginUser = new LoginRequestDTO();
        loginUser.setId("user");
        loginUser.setPassword("Pw1234!!");
        userService.signin(loginUser);
    }

    @Test
    @DisplayName("반려견 등록")
    void registerPet() {

        // 로그인
        LoginRequestDTO loginUser = new LoginRequestDTO();
        loginUser.setId("user");
        loginUser.setPassword("Pw1234!!");
        userService.signin(loginUser);

        //given
        PetDTO petDTO = new PetDTO("보리", "푸들", "M", true, null, 3);

        //when
        Long petId = petService.registerPet(loginUser.getId(), petDTO);

        //then
        assertEquals(petId, petRepository.findById(petId).get().getPetId());

   }

    @Test
    @DisplayName("반려견 수정")
    void updatePet() {
        //given
        Long petId = petRepository.findById(1L).get().getPetId();
        PetDTO petDTO = new PetDTO("율무", "푸들", "M", true, null, 3);

        //when
        petService.updatePet(petId, petDTO);

        //then
        assertEquals(petDTO.getName(), petRepository.findById(petId).get().getName());
    }

}