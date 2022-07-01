package com.matthewlaux.outliner.model;

import com.matthewlaux.outliner.TextBlock;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.*;

public class DocNode implements TreeNode {
    public final DocNode parent;
    public final List<DocNode> children = new ArrayList<>();

    DocNode(DocNode parent) {
        this.parent = parent;
    }

    public void addChild(DocNode child) {
        children.add(child);
    }

    public TreePath toTreePath() {
        List<DocNode> path = new ArrayList<>();
        DocNode node = this;
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return new TreePath(path.toArray(new DocNode[0]));
    }

    public String toHtml() {
        StringBuilder html = new StringBuilder();
        for (DocNode child : children) {
            html.append(child.toHtml());
        }
        return html.toString();
    }

    @Override
    public String toString() {
        return String.format("<%d children>", children.size());
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        for (int k = 0; k < children.size(); k++) {
            if (children.get(k) == node) {
                return k;
            }
        }
        return -1;
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration children() {
        final Iterator<DocNode> it = children.iterator();
        return new Enumeration() {
            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public Object nextElement() {
                return it.next();
            }
        };
    }
}
