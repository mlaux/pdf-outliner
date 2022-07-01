package com.matthewlaux.outliner.model;

class PageDocNode extends DocNode {
    public final String title;

    PageDocNode(DocNode parent, String title) {
        super(parent);
        this.title = title;
    }

    @Override
    public String toString() {
        return "Page: " + title;
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>")
                .append(title)
                .append("</title><style>body{font-family:sans-serif;}</style></head><body>")
                .append("<h1>").append(title).append("</h1>");
        for (DocNode child : children) {
            sb.append(child.toHtml());
        }
        sb.append("</body></html>");
        return sb.toString();
    }
}
