package com.aeon.hadog.repository;

import com.aeon.hadog.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByAdoptReviewReviewId(Long reviewId);
    List<ReviewComment> findByParentCommentCmtId(Long parentCommentId);
}
