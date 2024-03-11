package io.github.mds.cashflowweb.travel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class TravelFactory {

    public static TravelRequest createTravelRequest() {
        return new TravelRequest(
                LocalDate.of(2024, 3, 11),
                LocalDate.of(2024, 3, 14),
                "São Carlos",
                "Araraquara",
                "Description",
                new BigDecimal("1.00"),
                List.of("Ibate")
        );
    }

    public static TravelRequest createInvalidTravelRequest() {
        return new TravelRequest(null, null, null, null, null, null, null);
    }

}
