package com.aeon.hadog.service;

import com.aeon.hadog.base.code.ErrorCode;
import com.aeon.hadog.base.dto.comment.CommentDTO;
import com.aeon.hadog.base.exception.CommentNotFoundException;
import com.aeon.hadog.base.exception.UserNotFoundException;
import com.aeon.hadog.domain.ShelterPostComment;
import com.aeon.hadog.repository.ShelterPostCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShelterPostCommentService {

    private final ShelterPostCommentRepository shelterPostCmtRepository;
    private final UserService userService;

    // 게시글에 대한 댓글 조회
    public List<CommentDTO> getCommentByShelterPostId(Long shelterPostId) {
        List<ShelterPostComment> commentList = shelterPostCmtRepository.findByShelterPostId(shelterPostId);

        // 부모 댓글이 없는 댓글들만 필터링
        List<ShelterPostComment> rootComments = commentList.stream()
                .filter(comment -> comment.getParentComment() == null)
                .collect(Collectors.toList());

        return rootComments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO convertToDTO(ShelterPostComment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getShelterPostCommentId());
        dto.setPostId(comment.getShelterPostId());
        dto.setUserId(comment.getUserId());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setContent(comment.getContent());
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getShelterPostCommentId() : null);

        // 대댓글을 DTO로 변환
        List<CommentDTO> replies = comment.getReplies() != null
                ? comment.getReplies().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList())
                : null;
        dto.setReplies(replies);

        return dto;
    }
//    public List<CommentDTO> getCommentByShelterPostId(Long shelterPostId) {
//
//        List<ShelterPostComment> commentList = shelterPostCmtRepository.findByShelterPostId(shelterPostId);
//
//        List<CommentDTO> commentDtoList = commentList.stream().map(comment -> {
//
//            CommentDTO commentDTO = new CommentDTO();
//            commentDTO.setCommentId(comment.getShelterPostCommentId());
//            commentDTO.setPostId(comment.getShelterPostId());
//            commentDTO.setUserId(comment.getUserId());
//            commentDTO.setCreatedDate(comment.getCreatedDate());
//            commentDTO.setContent(comment.getContent());
//            if (comment.getParentComment() != null) {
//                commentDTO.setParentCommentId(comment.getParentComment().getShelterPostCommentId());
//            } else {
//                commentDTO.setParentCommentId(null);
//            }
//            if (comment.getReplies() != null) {
//                commentDTO.setReplies(comment.getReplies());
//            } else {
//                commentDTO.setReplies(null);
//            }
//
//            return commentDTO;
//
//        }).collect(Collectors.toList());
//
//        return commentDtoList;
//    }

    // 댓글 작성
    @Transactional
    public CommentDTO addComment(Long shelterPostId, String userId, String content, Long parentCommentId) {

        ShelterPostComment parentComment = null;

        if (parentCommentId != null) {
            parentComment = shelterPostCmtRepository.findById(parentCommentId)
                    .orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
        }

        CommentDTO commentDTO = CommentDTO.builder()
                .postId(shelterPostId)
                .userId(userId)
                .content(content)
                .createdDate(LocalDateTime.now())
                .parentCommentId(parentCommentId)
                .build();

        ShelterPostComment comment = ShelterPostComment.builder()
                .shelterPostId(shelterPostId)
                .userId(userId)
                .content(content)
                .createdDate(LocalDateTime.now())
                .parentComment(parentComment)
                .build();

        shelterPostCmtRepository.save(comment);

        return commentDTO;
    }

    // 댓글 삭제
    @Transactional
    public Long deleteComment(Long commentId, String userId) {


        if (shelterPostCmtRepository.existsById(commentId)) {

            ShelterPostComment comment = shelterPostCmtRepository.findByShelterPostCommentId(commentId);

            if (comment.getUserId().equals(userId)) {

                shelterPostCmtRepository.deleteById(commentId);
                return commentId;
            } else {
                throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
            }

        } else {
            throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }
    }
}
