package com.matthewlaux.outliner.model;

import com.matthewlaux.outliner.TextBlock;

public class DocEditor {
    private DocNode root;
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

    public void page(String title) {
        PageDocNode page = new PageDocNode(current, title);
        current.addChild(page);
        current = page;
    }

    public void section(String title) {
        SectionDocNode section = new SectionDocNode(current, title);
        current.addChild(section);
        current = section;
    }

    public void up() {
        if (current.parent != null) {
            current = current.parent;
        }
    }

    public void text(TextBlock text) {
        current.addText(text);
    }
}
