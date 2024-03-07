package io.github.mds.cashflowweb.employee;

import java.util.ArrayList;
import java.util.List;

public class NonUniqueEmployeeException extends RuntimeException {

    private final List<String> fields = new ArrayList<>();

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
