package com.aeon.hadog.controller;

import com.aeon.hadog.base.dto.comment.CommentDTO;
import com.aeon.hadog.base.dto.response.ResponseDTO;
import com.aeon.hadog.domain.ShelterPostComment;
import com.aeon.hadog.service.ShelterPostCommentService;
import com.aeon.hadog.service.ShelterPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private final ShelterPostCommentService commentService;

    // 댓글 작성
    @PostMapping("/addComment")
    public ResponseEntity<ResponseDTO> addComment(@AuthenticationPrincipal String userId, @RequestParam Long postId, @RequestParam String content, @RequestParam(required = false) Long parentCommentId) {
        CommentDTO commentDTO = commentService.addComment(postId, userId, content, parentCommentId);
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, true, "댓글 작성 성공", commentDTO));
    }

    // 특정 게시글에 대한 댓글 조회
    @GetMapping("/comment/{postId}")
    ResponseEntity<ResponseDTO> getComments(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentByShelterPostId(postId);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, true, "댓글 조회 성공", comments));
    }

    // 댓글 삭제
    @DeleteMapping("/delComment/{commentId}")
    public ResponseEntity<ResponseDTO> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal String userId) {
        Long deletedCommentId = commentService.deleteComment(commentId, userId);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, true, "댓글 삭제 성공", deletedCommentId));
    }

}
