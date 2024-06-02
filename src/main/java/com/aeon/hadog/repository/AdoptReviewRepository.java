package com.aeon.hadog.repository;

import com.aeon.hadog.domain.AdoptReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptReviewRepository extends JpaRepository<AdoptReview, Long> {

    List<AdoptReview> findAllByOrderByReviewDateDesc();
}
