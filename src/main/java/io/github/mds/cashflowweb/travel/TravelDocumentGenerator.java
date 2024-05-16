package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.expense.Expense;
import io.github.mds.cashflowweb.util.Document;
import io.github.mds.cashflowweb.util.DocumentGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class TravelDocumentGenerator {

    private final DocumentGenerator documentGenerator;

    public TravelDocumentGenerator(DocumentGenerator documentGenerator) {
        this.documentGenerator = documentGenerator;
    }

    public Document generateOrderDocument(Travel travel, List<Expense> expenses) {
        var template = "travel/travel-document";
        var variables = Map.of("travel", travel, "expenses", expenses);
        return documentGenerator.generate("viagem.pdf", template, variables, Locale.of("pt", "BR"));
    }

}