package com.aeon.hadog.base.dto.adopt_review;

import com.aeon.hadog.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDTO {

    @NotNull
    private Long imageId;

    private String fileName;
}
