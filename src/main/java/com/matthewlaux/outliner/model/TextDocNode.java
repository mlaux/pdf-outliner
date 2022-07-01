package com.matthewlaux.outliner.model;

import com.matthewlaux.outliner.TextBlock;

public class TextDocNode extends DocNode {
    public final TextBlock textBlock;

    public TextDocNode(DocNode parent, TextBlock text) {
        super(parent);
        this.textBlock = text;
    }

    @Override
    public String toString() {
        return textBlock.text;
    }

    @Override
    public String toHtml() {
        return textBlock.toHtml();
    }
}
