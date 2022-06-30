import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class RenderTest {
    private static final String TITLE = "PDF Outliner";
    private static final int SPACING = 12;

    private static PagePanel pagePanel;
    private static IMRenderer renderer;

    private static void createWindow() {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout mainLayout = new BorderLayout(0, SPACING);
        Border padding = BorderFactory.createEmptyBorder(SPACING, SPACING, SPACING, SPACING);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(mainLayout);
        mainPanel.setBorder(padding);

        pagePanel = new PagePanel(renderer);
        renderer.setOnPageCompleteListener(pagePanel);
        pagePanel.setPageNumber(430);

        mainPanel.add(pagePanel, BorderLayout.CENTER);

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
    }

    public static void main(String[] args) throws Exception {
        PDDocument document = PDDocument.load(new File("essentials.pdf"));
        renderer = new IMRenderer(document);
        SwingUtilities.invokeLater(RenderTest::createWindow);
    }
}
