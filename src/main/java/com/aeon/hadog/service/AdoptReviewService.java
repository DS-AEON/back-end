package com.aeon.hadog.service;

import com.aeon.hadog.domain.AdoptReview;
import com.aeon.hadog.domain.ReviewComment;
import com.aeon.hadog.domain.ReviewImage;
import com.aeon.hadog.repository.AdoptReviewRepository;
import com.aeon.hadog.repository.ReviewCommentRepository;
import com.aeon.hadog.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdoptReviewService {

    private final AdoptReviewRepository adoptReviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final AmazonS3Service amazonS3Service;

    @Transactional
    public AdoptReview saveReview(AdoptReview review, List<MultipartFile> images) throws Exception {
        AdoptReview savedReview = adoptReviewRepository.save(review);

        for (MultipartFile image : images) {
            String imageUrl = amazonS3Service.uploadImage(image);
            ReviewImage reviewImage = ReviewImage.builder()
                    .adoptReview(savedReview)
                    .fileName(imageUrl)
                    .build();
            reviewImageRepository.save(reviewImage);
        }

        return savedReview;
    }

    public Optional<AdoptReview> findById(Long id) {
        return adoptReviewRepository.findById(id);
    }

    public List<AdoptReview> findAll() {
        return adoptReviewRepository.findAll();
    }

    public List<ReviewComment> findCommentsByReviewId(Long reviewId) {
        return reviewCommentRepository.findByAdoptReviewReviewId(reviewId);
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
}
