package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class TravelFactory {

    private static final Faker faker = new Faker();

    private TravelFactory() {}

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

    public static Travel createRandomTravel(Employee employee) {
        return new Travel(
                faker.date().past(10, TimeUnit.DAYS).toLocalDateTime().toLocalDate(),
                faker.date().future(10, TimeUnit.DAYS).toLocalDateTime().toLocalDate(),
                faker.country().capital(),
                faker.country().capital(),
                faker.text().text(),
                BigDecimal.valueOf(faker.number().randomNumber()),
                List.of(faker.country().capital()),
                TravelStatus.SCHEDULED,
                employee
        );
    }

}
