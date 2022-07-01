package com.matthewlaux.outliner;

public enum TextType {
    Paragraph("<p>", "</p>", "pencil"), // regular body text
    CodeBlock("<pre>", "</pre>", "edit-code"),
    ParameterName("<table><tr><td><p><code>", "</code></p></td>", "function"),
    ParameterDescription("<td><p>", "</p></td></tr></table>", "function");

    public final String htmlStart;
    public final String htmlEnd;
    public final String iconName;

    TextType(String htmlStart, String htmlEnd, String iconName) {
        this.htmlStart = htmlStart;
        this.htmlEnd = htmlEnd;
        this.iconName = iconName;
    }
}