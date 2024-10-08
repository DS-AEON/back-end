package com.aeon.hadog.service;

import com.aeon.hadog.domain.AdoptReview;
import com.aeon.hadog.domain.ReviewComment;
import com.aeon.hadog.domain.ReviewImage;
import com.aeon.hadog.repository.AdoptReviewRepository;
import com.aeon.hadog.repository.ReviewCommentRepository;
import com.aeon.hadog.repository.ReviewImageRepository;
import com.aeon.hadog.base.dto.adopt_review.ReviewCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdoptReviewService {

    private final AdoptReviewRepository adoptReviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final AmazonS3Service amazonS3Service;

    @Transactional
    public AdoptReview saveReview(AdoptReview review, List<MultipartFile> images) throws Exception {
        if (images == null) {
            images = new ArrayList<>();
        }
        review.setImages(new ArrayList<>());

        AdoptReview savedReview = adoptReviewRepository.save(review);

        for (MultipartFile image : images) {
            String imageUrl = amazonS3Service.uploadImage(image);
            ReviewImage reviewImage = ReviewImage.builder()
                    .adoptReview(savedReview)
                    .fileName(imageUrl)
                    .build();

            reviewImageRepository.save(reviewImage);
            review.getImages().add(reviewImage); // 이미지 리스트에 이미지 추가
        }

        return savedReview;
    }

    public Optional<AdoptReview> findById(Long id) {
        return adoptReviewRepository.findById(id);
    }

    public List<AdoptReview> findAll() {
        return adoptReviewRepository.findAll();
    }

    public List<ReviewCommentDTO> findCommentsByReviewId(Long reviewId) {
        List<ReviewComment> comments = reviewCommentRepository.findByAdoptReviewReviewId(reviewId);
        return comments.stream()
                .map(comment -> ReviewCommentDTO.builder()
                        .cmtId(comment.getCmtId())
                        .content(comment.getContent())
                        .userId(comment.getUser().getId())  // 사용자 ID를 포함
                        .cmtDate(comment.getCmtDate())
                        .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getCmtId() : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewComment saveComment(ReviewComment comment) {
        return reviewCommentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        reviewCommentRepository.deleteById(commentId);
    }

    public List<ReviewComment> findAllCommentsByReviewId(Long reviewId) {
        return reviewCommentRepository.findByAdoptReviewReviewId(reviewId);
    }

    public List<ReviewComment> findRepliesByParentCommentId(Long parentCommentId) {
        return reviewCommentRepository.findByParentCommentCmtId(parentCommentId);
    }

    public List<AdoptReview> findReviewsByUserId(String userId) {
        return adoptReviewRepository.findByUser_IdOrderByReviewDateDesc(userId);
    }
}
