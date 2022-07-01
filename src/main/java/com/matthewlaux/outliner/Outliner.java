package com.matthewlaux.outliner;

import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class Outliner {
    private static final String TITLE = "PDF Outliner";

    private static PagePanel pagePanel;
    private static IMRenderer renderer;
    private static JTree tree;

    private static void createWindow() {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout mainLayout = new BorderLayout(0, 0);
        JPanel mainPanel = new JPanel(mainLayout);

        pagePanel = new PagePanel(renderer);
        renderer.setOnPageCompleteListener(pagePanel);

        mainPanel.add(pagePanel, BorderLayout.CENTER);

        tree = new JTree();
        tree.setPreferredSize(new Dimension(250, 0));
        mainPanel.add(tree, BorderLayout.EAST);

        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        mainPanel.requestFocus();
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    pagePanel.previousPage();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    pagePanel.nextPage();
                } else if (e.getKeyCode() == KeyEvent.VK_N) {
                    pagePanel.nextBlock(TextType.Text);
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    pagePanel.previousBlock();
                }
            }
        });

        pagePanel.goToPage(430);
    }

    public static void main(String[] args) throws Exception {
        PDDocument document = PDDocument.load(new File("essentials.pdf"));
        renderer = new IMRenderer(document);
        SwingUtilities.invokeLater(Outliner::createWindow);
    }
}
