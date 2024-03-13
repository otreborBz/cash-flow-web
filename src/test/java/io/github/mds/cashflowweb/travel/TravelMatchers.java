package io.github.mds.cashflowweb.travel;

import java.util.Map;

public final class TravelMatchers {

    private TravelMatchers() {}

    public static Map<String, String> travel(Travel travel) {
        return Map.of(
                "startDate", travel.getStartDate().toString(),
                "destination", travel.getDestination(),
                "description", travel.getDescription()
        );
    }

}
