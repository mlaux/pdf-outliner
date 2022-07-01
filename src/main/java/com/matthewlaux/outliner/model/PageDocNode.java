package com.matthewlaux.outliner.model;

class PageDocNode extends DocNode {
    public final String title;

    PageDocNode(DocNode parent, String title) {
        super(parent);
        this.title = title;
    }
}
