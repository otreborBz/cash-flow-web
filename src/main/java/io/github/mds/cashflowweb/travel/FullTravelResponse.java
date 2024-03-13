package io.github.mds.cashflowweb.travel;

import java.time.LocalDate;

public record FullTravelResponse(
    LocalDate startDate,
    LocalDate endDate,
    String origin,
    String destination,
    String description
) {

}
