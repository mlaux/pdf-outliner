package com.matthewlaux.outliner;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class IMPageDrawer extends PageDrawer {
    private static final byte[] tempch = new byte[1];

    private final IMRenderer.PageListener pageListener;

    private Rectangle2D lastBBox;
    private Rectangle2D wholeBBox;
    private String wholeText = "";
    private PDFont lastFont;
    private List<TextBlock> blocks = new ArrayList<>();

    private SettingsPanel.Settings settings;

    public IMPageDrawer(PageDrawerParameters parameters, IMRenderer.PageListener pageListener, SettingsPanel.Settings settings) throws IOException {
        super(parameters);
        this.pageListener = pageListener;
        this.settings = settings;
    }

    @Override
    public void drawPage(Graphics g, PDRectangle pageSize) throws IOException {
        blocks = new ArrayList<>();
        super.drawPage(g, pageSize);
    }

    @Override
    protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, Vector displacement) throws IOException {
        super.showGlyph(textRenderingMatrix, font, code, displacement);

        Rectangle2D box = new Rectangle2D.Double(0, 0, font.getWidth(code) / 1000.0, 1);
        AffineTransform at = textRenderingMatrix.createAffineTransform();
        box = at.createTransformedShape(box).getBounds2D();

        double distX = 0;
        double distY = 0;
        if (lastBBox != null) {
            distX = distanceX(lastBBox, box);
            distY = distanceY(lastBBox, box);
        }

        if (distX > settings.newlineX) {
            wholeText += '\n';
        } else if (distX > settings.spaceX) {
            wholeText += ' ';
        }

        if (distX == 0 && distY == 0) {
            wholeBBox = box;
            wholeText += convertMac(code);
        } else if (distY >= settings.paragraphSpacing || (distX > settings.columnMin && distX < settings.columnMax && font != lastFont)) {
            if (settings.showBBoxes) {
                drawRect(wholeBBox);
            }
            blocks.add(new TextBlock(wholeBBox, wholeText));
            wholeText = convertMac(code);
            wholeBBox = box;
        } else {
            wholeBBox = wholeBBox.createUnion(box);
            wholeText += convertMac(code);
        }
        lastBBox = box;
        lastFont = font;
    }

    @Override
    public void processPage(PDPage page) throws IOException {
        super.processPage(page);
        if (wholeBBox != null) {
            // the final one
            if (settings.showBBoxes) {
                drawRect(wholeBBox);
            }
            blocks.add(new TextBlock(wholeBBox, wholeText));
        }
        if (pageListener != null) {
            pageListener.onPageComplete(blocks, getGraphics().getTransform());
        }
    }

    private void drawRect(Rectangle2D rect) {
        Graphics2D graphics = getGraphics();
        Color color = graphics.getColor();
        Stroke stroke = graphics.getStroke();
        Shape clip = graphics.getClip();
        // draw
        graphics.setClip(graphics.getDeviceConfiguration().getBounds());
        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(.5f));
        graphics.draw(rect);
        // restore
        graphics.setStroke(stroke);
        graphics.setColor(color);
        graphics.setClip(clip);
    }

    // return Math.sqrt(((a.getMaxX() - b.getMinX()) * (a.getMaxX() - b.getMinX())) + ((a.getMaxY() - b.getMinY()) * (a.getMaxY() - b.getMinY())));
    private static double distanceY(Rectangle2D a, Rectangle2D b) {
        return Math.abs(b.getMinY() - a.getMaxY());
    }

    private static double distanceX(Rectangle2D a, Rectangle2D b) {
        return Math.abs(b.getMinX() - a.getMaxX());
    }

    private static String convertMac(int code) {
        tempch[0] = (byte) code;
        try {
            return new String(tempch, 0, tempch.length, "MacRoman");
        } catch (UnsupportedEncodingException e) {
            return String.valueOf((char) code);
        }
    }
}
