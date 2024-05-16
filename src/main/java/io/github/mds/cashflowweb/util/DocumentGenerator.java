package io.github.mds.cashflowweb.util;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Map;

@Component
public class DocumentGenerator {

    private final TemplateEngine templateEngine;

    public DocumentGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public Document generate(String filename, String template, Map<String, Object> variables, Locale locale) {
        var html = templateEngine.process(template, contextFromMap(variables, locale));
        var content = new ByteArrayOutputStream();
        var textRenderer = new ITextRenderer();
        textRenderer.setDocumentFromString(html);
        textRenderer.layout();
        textRenderer.createPDF(content);
        return new Document(filename, content, content.size());
    }

    private Context contextFromMap(Map<String, Object> variables, Locale locale) {
        var context = new Context(locale);
        context.setVariables(variables);
        return context;
    }

}