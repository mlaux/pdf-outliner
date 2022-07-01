package com.matthewlaux.outliner.model;

import com.matthewlaux.outliner.TextBlock;

public class DocEditor {
    private final DocNode root;
    private DocNode current;

    public DocEditor() {
        root = new DocNode(null);
        current = root;
    }

    public DocNode getRoot() {
        return root;
    }

    public DocNode getCurrent() {
        return current;
    }

    public DocNode getCurrentPage() {
        DocNode node = current;
        while (node != null && !(node instanceof PageDocNode)) {
            node = node.parent;
        }
        return node;
    }

    public void page(String title) {
        // pages always go on the root
        PageDocNode page = new PageDocNode(root, title);
        root.addChild(page);
        current = page;
    }

    public void section(String title) {
        if (current == root) {
            return;
        }

        DocNode useParent;
        if (current instanceof SectionDocNode) {
            useParent = current.parent;
        } else {
            useParent = current;
        }
        SectionDocNode section = new SectionDocNode(useParent, title);
        useParent.addChild(section);
        current = section;
    }

    public void up() {
        if (current.parent != null) {
            current = current.parent;
        }
    }

    public void text(TextBlock text) {
        TextDocNode node = new TextDocNode(current, text);
        current.addChild(node);
    }
}
