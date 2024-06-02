package com.aeon.hadog.controller;

import com.aeon.hadog.base.dto.adopt_review.AdoptReviewDTO;
import com.aeon.hadog.base.dto.adopt_review.ReviewCommentDTO;
import com.aeon.hadog.base.dto.adopt_review.ReviewImageDTO;
import com.aeon.hadog.base.dto.response.ResponseDTO;
import com.aeon.hadog.domain.AdoptReview;
import com.aeon.hadog.domain.ReviewComment;
import com.aeon.hadog.domain.User;
import com.aeon.hadog.service.AdoptReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class AdoptReviewController {

    private final AdoptReviewService adoptReviewService;

    @PostMapping
    public ResponseEntity<ResponseDTO<AdoptReviewDTO>> createReview(@AuthenticationPrincipal User user,
                                                                    @RequestPart AdoptReviewDTO reviewDTO,
                                                                    @RequestPart List<MultipartFile> images) {
        try {
            AdoptReview review = AdoptReview.builder()
                    .user(user)
                    .reviewDate(LocalDateTime.now())
                    .content(reviewDTO.getContent())
                    .build();

            AdoptReview savedReview = adoptReviewService.saveReview(review, images);

            AdoptReviewDTO responseDTO = AdoptReviewDTO.builder()
                    .reviewId(savedReview.getReviewId())
                    .reviewDate(savedReview.getReviewDate())
                    .content(savedReview.getContent())
                    .images(savedReview.getImages().stream().map(image ->
                            ReviewImageDTO.builder()
                                    .imageId(image.getImageId())
                                    .fileName(image.getFileName())
                                    .build()
                    ).collect(Collectors.toList()))
                    .build();

            return ResponseEntity.ok(new ResponseDTO<>(200, true, "입양후기 등록 성공", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO<>(400, false, "입양후기 등록 실패: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AdoptReviewDTO>> getReview(@PathVariable Long id) {
        return adoptReviewService.findById(id)
                .map(review -> {
                    AdoptReviewDTO responseDTO = AdoptReviewDTO.builder()
                            .reviewId(review.getReviewId())
                            .reviewDate(review.getReviewDate())
                            .content(review.getContent())
                            .images(review.getImages().stream().map(image ->
                                    ReviewImageDTO.builder()
                                            .imageId(image.getImageId())
                                            .fileName(image.getFileName())
                                            .build()
                            ).collect(Collectors.toList()))
                            .build();

                    return ResponseEntity.ok(new ResponseDTO<>(200, true, "입양후기 조회 성공", responseDTO));
                })
                .orElseGet(() -> ResponseEntity.ok(new ResponseDTO<>(400, false, "입양후기 조회 실패: 후기 없음", null)));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AdoptReviewDTO>>> getAllReviews() {
        List<AdoptReviewDTO> reviews = adoptReviewService.findAll().stream()
                .map(review -> AdoptReviewDTO.builder()
                        .reviewId(review.getReviewId())
                        .reviewDate(review.getReviewDate())
                        .content(review.getContent())
                        .images(review.getImages().stream().map(image ->
                                ReviewImageDTO.builder()
                                        .imageId(image.getImageId())
                                        .fileName(image.getFileName())
                                        .build()
                        ).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO<>(200, true, "입양후기 목록 조회 성공", reviews));
    }

    @PostMapping("/{reviewId}/comments")
    public ResponseEntity<ResponseDTO<ReviewCommentDTO>> addComment(@PathVariable Long reviewId,
                                                                    @RequestBody ReviewCommentDTO commentDTO) {
        try {
            ReviewComment comment = ReviewComment.builder()
                    .adoptReview(AdoptReview.builder().reviewId(reviewId).build())
                    .content(commentDTO.getContent())
                    .cmtDate(LocalDateTime.now())
                    .parentComment(commentDTO.getParentCommentId() != null ? ReviewComment.builder().cmtId(commentDTO.getParentCommentId()).build() : null)
                    .build();

            ReviewComment savedComment = adoptReviewService.saveComment(comment);

            ReviewCommentDTO responseDTO = ReviewCommentDTO.builder()
                    .cmtId(savedComment.getCmtId())
                    .content(savedComment.getContent())
                    .cmtDate(savedComment.getCmtDate())
                    .parentCommentId(savedComment.getParentComment() != null ? savedComment.getParentComment().getCmtId() : null)
                    .build();

            return ResponseEntity.ok(new ResponseDTO<>(200, true, "댓글 등록 성공", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO<>(400, false, "댓글 등록 실패: " + e.getMessage(), null));
        }
    }


    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<ResponseDTO<List<ReviewCommentDTO>>> getComments(@PathVariable Long reviewId) {
        List<ReviewCommentDTO> comments = adoptReviewService.findAllCommentsByReviewId(reviewId).stream()
                .map(comment -> ReviewCommentDTO.builder()
                        .cmtId(comment.getCmtId())
                        .content(comment.getContent())
                        .cmtDate(comment.getCmtDate())
                        .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getCmtId() : null)
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO<>(200, true, "댓글 목록 조회 성공", comments));
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<ResponseDTO<List<ReviewCommentDTO>>> getReplies(@PathVariable Long commentId) {
        List<ReviewCommentDTO> replies = adoptReviewService.findRepliesByParentCommentId(commentId).stream()
                .map(reply -> ReviewCommentDTO.builder()
                        .cmtId(reply.getCmtId())
                        .content(reply.getContent())
                        .cmtDate(reply.getCmtDate())
                        .parentCommentId(reply.getParentComment() != null ? reply.getParentComment().getCmtId() : null)
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO<>(200, true, "대댓글 목록 조회 성공", replies));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> deleteComment(@PathVariable Long commentId) {
        try {
            adoptReviewService.deleteComment(commentId);
            return ResponseEntity.ok(new ResponseDTO<>(200, true, "댓글 삭제 성공", null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO<>(400, false, "댓글 삭제 실패: " + e.getMessage(), null));
        }
    }
}

