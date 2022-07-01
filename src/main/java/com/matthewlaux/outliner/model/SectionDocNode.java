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

    @Override
    public String getIconName() {
        return "edit-heading-1";
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>").append(title).append("</h2>");
        for (DocNode child : children) {
            sb.append(child.toHtml());
        }
        return sb.toString();
    }
}
