import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.List;

public class IMRenderer extends PDFRenderer {
    private OnPageCompleteListener onPageCompleteListener;

    public IMRenderer(PDDocument document) {
        super(document);
    }

    @Override
    protected PageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
        return new IMPageDrawer(parameters, onPageCompleteListener);
    }

    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }

    interface OnPageCompleteListener {
        void onPageComplete(List<TextBlock> textBlocks, AffineTransform transform);
    }
}
