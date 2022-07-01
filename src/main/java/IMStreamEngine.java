import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.*;

public class IMStreamEngine extends PDFTextStripper {
    private static final String FONT_CODE = "F21";
    private static final String FONT_BODY = "F24";
    private static final List<String> FONT_IGNORE = Arrays.asList("F4", "F5", "F6");

    private String lastFont;
    private Set<String> symbols = new HashSet<>();

    public IMStreamEngine() throws IOException {
    }

    @Override
    protected void showText(byte[] string) throws IOException {
        super.showText(string);

        PDTextState textState = this.getGraphicsState().getTextState();
        float fontSize = textState.getFontSize();
        float horizontalScaling = textState.getHorizontalScaling() / 100.0F;
        PDFont font = textState.getFont();
        //System.out.println(font.getName() + " " + textState.getFontSize());
        //System.out.println(new String(string));
        String text = new String(string);
        if (FONT_CODE.equals(lastFont) && isSymbol(text)) {
            symbols.add(text);
        }
    }

    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        super.processOperator(operator, operands);
        if (OperatorName.SET_FONT_AND_SIZE.equals(operator.getName())) {
            lastFont = ((COSName) operands.get(0)).getName();
        }
        //System.out.println(operator.getName());
    }

    @Override
    protected void writeParagraphStart() throws IOException {
        super.writeParagraphStart();
        //System.out.println("new paragraph, last font: " + lastFont);
    }

    private boolean isSymbol(String text) {
        String[] parts = text.split(" ");
        return parts.length == 1 && parts[0].matches("[A-Za-z][A-Za-z0-9]+") && !parts[0].matches("[A-Z]+");
    }

    public Set<String> getSymbols() {
        return symbols;
    }
}
