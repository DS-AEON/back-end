package com.aeon.hadog.service;

import com.aeon.hadog.base.code.ErrorCode;
import com.aeon.hadog.base.dto.diary.DiaryDTO;
import com.aeon.hadog.base.exception.*;
import com.aeon.hadog.domain.Diary;
import com.aeon.hadog.domain.EmotionTrack;
import com.aeon.hadog.domain.Pet;
import com.aeon.hadog.domain.User;
import com.aeon.hadog.repository.DiaryRepository;
import com.aeon.hadog.repository.EmotionTrackRepository;
import com.aeon.hadog.repository.PetRepository;
import com.aeon.hadog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    private final EmotionTrackRepository emotionTrackRepository;

    private final PetRepository petRepository;

    public Long createDiary(String userId, DiaryDTO diaryDTO){

        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        EmotionTrack emotionTrack = emotionTrackRepository.findByEmotionTrackId(diaryDTO.getEmotionTrackId()).orElseThrow(()->new EmotionTrackNotFoundException(ErrorCode.EMOTIONTRACK_NOT_FOUND));
        Pet pet = petRepository.findByPetId(emotionTrack.getPetId()).orElseThrow();

        if (!Objects.equals(user, pet.getUser())) {
            throw new EmotionTrackNotBelongToUserException(ErrorCode.EMOTION_TRACK_NOT_BELONG_TO_USER_ERROR);
        }

        Diary diary = Diary.builder()
                .emotionTrack(emotionTrack)
                .diaryDate(diaryDTO.getDiaryDate())
                .content(diaryDTO.getContent())
                .build();

        diaryRepository.save(diary);

        return diary.getDiaryId();
    }

    public DiaryDTO getDiary(String userId, Long diaryId){
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        Diary diary = diaryRepository.findByDiaryId(diaryId).orElseThrow(()->new DiaryNotFoundException(ErrorCode.DIARY_NOT_FOUND));
        Pet pet = petRepository.findByPetId(diary.getEmotionTrack().getPetId()).orElseThrow();

        if (!Objects.equals(user, pet.getUser())) {
            throw new EmotionTrackNotBelongToUserException(ErrorCode.EMOTION_TRACK_NOT_BELONG_TO_USER_ERROR);
        }

        DiaryDTO diaryDTO = DiaryDTO.builder()
                .emotionTrackId(diary.getEmotionTrack().getEmotionTrackId())
                .diaryDate(diary.getDiaryDate())
                .content(diary.getContent())
                .build();

        return diaryDTO;
    }

    public DiaryDTO modifyDiary(String userId, Long diaryId, String content){
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        Diary diary = diaryRepository.findByDiaryId(diaryId).orElseThrow(()->new DiaryNotFoundException(ErrorCode.DIARY_NOT_FOUND));
        Pet pet = petRepository.findByPetId(diary.getEmotionTrack().getPetId()).orElseThrow();

        if (!Objects.equals(user, pet.getUser())) {
            throw new EmotionTrackNotBelongToUserException(ErrorCode.EMOTION_TRACK_NOT_BELONG_TO_USER_ERROR);
        }

        if (content == null || content.trim().isEmpty()) {
            throw new BlanckContentException(ErrorCode.BLANK_CONTENT_ERROR);
        }

        diary.setContent(content);
        diaryRepository.save(diary);

        DiaryDTO diaryDTO = DiaryDTO.builder()
                .emotionTrackId(diary.getEmotionTrack().getEmotionTrackId())
                .diaryDate(diary.getDiaryDate())
                .content(diary.getContent())
                .build();

        return diaryDTO;
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

}
