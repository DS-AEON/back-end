package com.aeon.hadog.base.dto.comment;

import com.aeon.hadog.domain.ShelterPostComment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentDTO {

    @NotNull
    private Long commentId;

    @NotNull
    private Long postId;

    @NotNull
    private String userId;

    @NotNull
    private LocalDateTime createdDate;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    private Long parentCommentId; // 부모 댓글

    private List<CommentDTO> replies; // 대댓글 목록
}
