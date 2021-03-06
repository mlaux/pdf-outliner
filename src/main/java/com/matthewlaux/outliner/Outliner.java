package com.matthewlaux.outliner;

import com.matthewlaux.outliner.model.DocEditor;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Outliner {
    private static final String TITLE = "PDF Outliner";

    private static PagePanel pagePanel;
    private static IMRenderer renderer;
    private static JTree tree;
    private static DocEditor editor;
    private static DefaultTreeModel treeModel;
    private static JEditorPane preview;

    private static void createWindow() {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout mainLayout = new BorderLayout(0, 0);
        JPanel mainPanel = new JPanel(mainLayout);

        pagePanel = new PagePanel(renderer);
        renderer.setPageListener(pagePanel);

        mainPanel.add(pagePanel, BorderLayout.CENTER);

        editor = new DocEditor();
        tree = new JTree(editor.getRoot());
        treeModel = (DefaultTreeModel) tree.getModel();
        tree.setPreferredSize(new Dimension(250, 0));
        mainPanel.add(new JScrollPane(tree), BorderLayout.WEST);

        preview = new JEditorPane();
        preview.setPreferredSize(new Dimension(300, 0));
        preview.setContentType("text/html");
        mainPanel.add(new JScrollPane(preview), BorderLayout.EAST);

        SettingsPanel sp = new SettingsPanel();
        sp.setSettingsChangeListener(pagePanel);
        mainPanel.add(sp.getPanel(), BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        mainPanel.requestFocus();
        mainPanel.addKeyListener(new ControlKeyAdapter());

        pagePanel.goToPage(430);
    }

    private static void expandTree() {
        for (int k = 0; k < tree.getRowCount(); k++) {
            tree.expandRow(k);
        }
    }

    private static void refreshView() {
        treeModel.reload(editor.getRoot());
        expandTree();
        tree.setSelectionPath(editor.getCurrent().toTreePath());
        if (editor.getCurrentPage() != null) {
            preview.setText(editor.getCurrentPage().toHtml());

            // scroll to bottom
            Document doc = preview.getDocument();
            preview.select(doc.getLength(), doc.getLength());
        }
    }

    public static void main(String[] args) throws Exception {
        PDDocument document = PDDocument.load(new File("essentials.pdf"));
        renderer = new IMRenderer(document);
        SwingUtilities.invokeLater(Outliner::createWindow);
    }

    static class ControlKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:  pagePanel.previousPage(); break;
                case KeyEvent.VK_RIGHT: pagePanel.nextPage();     break;
                case KeyEvent.VK_N:     pagePanel.nextBlock();    break;
                case KeyEvent.VK_P:
                    editor.page(pagePanel.getCurrentBlock().text);
                    pagePanel.nextBlock();
                    break;
                case KeyEvent.VK_S:
                    editor.section(pagePanel.getCurrentBlock().text);
                    pagePanel.nextBlock();
                    break;
                case KeyEvent.VK_U:
                    editor.up();
                    break;
                default:
                    TextType type = KEY_TYPE_MAP.get(e.getKeyCode());
                    if (type != null) {
                        pagePanel.getCurrentBlock().type = type;
                        editor.text(pagePanel.getCurrentBlock());
                        pagePanel.nextBlock();
                    }
            }
            refreshView();
        }
    }

    private static final Map<Integer, TextType> KEY_TYPE_MAP = new HashMap<>();
    static {
        KEY_TYPE_MAP.put(KeyEvent.VK_C, TextType.CodeBlock);
        KEY_TYPE_MAP.put(KeyEvent.VK_1, TextType.ParameterName);
        KEY_TYPE_MAP.put(KeyEvent.VK_2, TextType.ParameterDescription);
        KEY_TYPE_MAP.put(KeyEvent.VK_T, TextType.Paragraph);
    }
}
