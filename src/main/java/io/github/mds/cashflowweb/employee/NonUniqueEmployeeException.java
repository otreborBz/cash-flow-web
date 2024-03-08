package io.github.mds.cashflowweb.employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NonUniqueEmployeeException extends RuntimeException {

    private final List<String> fields = new ArrayList<>();

    public NonUniqueEmployeeException(String... fields) {
        this.fields.addAll(Arrays.asList(fields));
    }

    public void addField(String field) {
        fields.add(field);
    }

    public List<String> getFields() {
        return fields.stream().toList();
    }

    public boolean hasFields() {
        return !fields.isEmpty();
    }

}
