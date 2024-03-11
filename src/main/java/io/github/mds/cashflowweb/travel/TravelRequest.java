package io.github.mds.cashflowweb.travel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TravelRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotBlank String origin,
        @NotBlank String destination,
        @NotBlank String description,
        BigDecimal budget,
        @UniqueElements @NotEmpty List<String> itinerary
) {

    public Travel toEntity() {
        return new Travel(
                startDate,
                endDate,
                origin,
                destination,
                description,
                budget,
                itinerary,
                TravelStatus.SCHEDULED
        );
    }

}
