package io.github.mds.cashflowweb.travel;

import java.time.LocalDate;

public record TravelResponse(
        LocalDate startDate,
        String destination,
        String description
) {

}
