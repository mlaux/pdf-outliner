package com.matthewlaux.outliner;

public enum TextType {
    Paragraph("<p>", "</p>"), // regular body text
    CodeBlock("<pre>", "</pre>"),
    ParameterName("<table><tr><td><p><code>", "</code></p></td>"),
    ParameterDescription("<td><p>", "</p></td></tr></table>");

    public final String htmlStart;
    public final String htmlEnd;

    TextType(String htmlStart, String htmlEnd) {
        this.htmlStart = htmlStart;
        this.htmlEnd = htmlEnd;
    }
}