package com.matthewlaux.outliner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.List;

public class IMRenderer extends PDFRenderer {
    private PageListener pageListener;

    public IMRenderer(PDDocument document) {
        super(document);
    }

    @Override
    protected PageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
        return new IMPageDrawer(parameters, pageListener, SettingsPanel.getSettings());
    }

    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    interface PageListener {
        void onPageComplete(List<TextBlock> textBlocks, AffineTransform transform);
    }
}
