import java.awt.geom.Rectangle2D;

public class TextBlock {
    public final Rectangle2D bounds;
    public final String text;

    public TextType type;

    TextBlock(Rectangle2D bounds, String text) {
        this.bounds = bounds;
        this.text = text;
    }

    public void setType(TextType type) {
        this.type = type;
    }
}