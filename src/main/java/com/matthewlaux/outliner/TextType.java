package com.matthewlaux.outliner;

public enum TextType {
    Paragraph("p"), // regular body text
    CodeBlock("pre"),
    ParameterName("code"),
    ParameterDescription(null);

    public final String htmlTag;

    TextType(String htmlTag) {
        this.htmlTag = htmlTag;
    }
}