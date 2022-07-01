package com.matthewlaux.outliner.model;

class TableDocNode extends DocNode {
    public final int numRows;
    public final int numCols;

    TableDocNode(DocNode parent, int numRows, int numCols) {
        super(parent);
        this.numRows = numRows;
        this.numCols = numCols;
    }
}
