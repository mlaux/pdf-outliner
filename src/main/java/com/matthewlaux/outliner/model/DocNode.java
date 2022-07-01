package com.matthewlaux.outliner.model;

import com.matthewlaux.outliner.TextBlock;

import java.util.ArrayList;
import java.util.List;

class DocNode {
    public final DocNode parent;
    public final List<DocNode> children = new ArrayList<>();
    public final List<TextBlock> textBlocks = new ArrayList<>();

    DocNode(DocNode parent) {
        this.parent = parent;
    }

    public void addChild(DocNode child) {
        children.add(child);
    }

    public void addText(TextBlock text) {
        textBlocks.add(text);
    }
}
