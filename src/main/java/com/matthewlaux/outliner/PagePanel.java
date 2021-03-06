package com.matthewlaux.outliner;

import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PagePanel extends JPanel implements IMRenderer.PageListener, SettingsPanel.SettingsChangeListener {
    private final PDFRenderer renderer;
    private int pageNumber;
    private List<TextBlock> blocks = new ArrayList<>();
    private int blockIndex;

    private BufferedImage currentPage;
    private AffineTransform transform;

    public PagePanel(PDFRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (currentPage != null) {
            g.drawImage(currentPage, 0, 0, null);
        }
        g.setColor(new Color(255, 0, 0, 64));
        AffineTransform at = g.getTransform();
        g.setTransform(transform);
        if (!blocks.isEmpty()) {
            g.fill(blocks.get(blockIndex).bounds);
        }
        g.setTransform(at);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(612, 792);
    }

    @Override
    public void onPageComplete(List<TextBlock> blocks, AffineTransform transform) {
        this.blocks = blocks;
        this.transform = transform;
        this.blockIndex = 0;
        repaint();
    }

    @Override
    public void onSettingsChange(SettingsPanel.Settings settings) {
        tryRender();
    }

    public void goToPage(int pageNumber) {
        this.pageNumber = pageNumber;
        tryRender();
    }

    public void nextPage() {
        goToPage(pageNumber + 1);
    }

    public void previousPage() {
        goToPage(pageNumber - 1);
    }

    public void nextBlock() {
        if (blockIndex == blocks.size() - 1) {
            nextPage();
            return;
        }
        blockIndex++;
        repaint();
    }

    public void previousBlock() {
        if (blockIndex == 0) {
            previousPage();
            return;
        }
        blockIndex--;
        repaint();
    }

    public TextBlock getCurrentBlock() {
        return blocks.get(blockIndex);
    }

    private void tryRender() {
        try {
            currentPage = renderer.renderImage(pageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
