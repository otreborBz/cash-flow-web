package io.github.mds.cashflowweb.travel;

import java.time.LocalDate;

public record FullTravelResponse(
    long id,
    LocalDate startDate,
    LocalDate endDate,
    String origin,
    String destination,
    String description
) {

}
