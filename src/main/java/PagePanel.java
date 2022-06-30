import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class PagePanel extends JPanel implements IMRenderer.OnPageCompleteListener {
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
        if (blocks.size() > 0) {
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
        System.out.println("setBlocks");
        this.blocks = blocks;
        this.transform = transform;
        this.blockIndex = 0;
        repaint();
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        tryRender();
    }

    public void nextPage() {
        setPageNumber(pageNumber + 1);
    }

    public void previousPage() {
        setPageNumber(pageNumber - 1);
    }

    public void nextBlock(TextType type) {
        if (blockIndex == blocks.size() - 1) {
            nextPage();
            return;
        }
        blocks.get(blockIndex).type = type;
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

    private void tryRender() {
        try {
            currentPage = renderer.renderImage(pageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
