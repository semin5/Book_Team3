package com.sec.dto;

import com.sec.entity.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class MapRequest {
    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;
    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;
    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    public Map toDocument(int postId) {
        return Map.builder()
                .postId(postId)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
