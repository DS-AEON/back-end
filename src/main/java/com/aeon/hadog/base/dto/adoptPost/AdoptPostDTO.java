package com.aeon.hadog.base.dto.adoptPost;

import com.aeon.hadog.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdoptPostDTO {

    private Long adoptPostId;

    private LocalDateTime postDate;

    private List<String> imageUrls;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "종은 필수 입력 값입니다.")
    private String breed;
    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String sex;


    @NotBlank(message = "나이는 필수 입력 값입니다.")
    private String age;


    @NotBlank(message = "공고 번호는 필수 입력 값입니다.")
    private String phone;

    @NotBlank(message = "입양 기간는 필수 입력 값입니다.")
    private String duration;

    @NotNull(message = "중성화 여부는 필수 입력 값입니다.")
    private boolean neutering;

    private boolean adoptStatus;

    private User user;

}
