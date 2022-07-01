package com.matthewlaux.outliner.model;

class SectionDocNode extends DocNode {
    public final String title;

    SectionDocNode(DocNode parent, String title) {
        super(parent);
        this.title = title;
    }

    @Override
    public String toString() {
        return "Section: " + title;
    }
}
