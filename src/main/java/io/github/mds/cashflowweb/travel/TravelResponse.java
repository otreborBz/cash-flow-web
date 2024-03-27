package io.github.mds.cashflowweb.travel;

import java.time.LocalDate;

public record TravelResponse(
        Long id,
        LocalDate startDate,
        String destination,
        String description
) {

}
