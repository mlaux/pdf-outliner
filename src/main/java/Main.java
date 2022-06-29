import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final int SUMMARY = 0;
    private static final int DESCRIPTION = 1;
    private static final int SPECIAL_CONSIDERATIONS = 2;
    private static final int ASSEMBLY_INFO = 3;
    private static final int RESULT_CODES = 4;
    private static final int SEE_ALSO = 5;

    private static Set<String> symbols = new HashSet<>();
    private static List<Entry> entries = new ArrayList<>();

    static class Entry implements Comparable<Entry> {
        public String funcName = "";
        public String summary = "";
        public String description = "";
        public String specialConsiderations = "";
        public String assemblyInformation = "";
        public String resultCodes = "";
        public String seeAlso = "";

        @Override
        public String toString() {
            return "Entry{" +
                    "funcName='" + funcName + '\'' +
                    ", summary='" + summary + '\'' +
                    ", description='" + description + '\'' +
                    ", specialConsiderations='" + specialConsiderations + '\'' +
                    ", assemblyInformation='" + assemblyInformation + '\'' +
                    ", resultCodes='" + resultCodes + '\'' +
                    ", seeAlso='" + seeAlso + '\'' +
                    '}';
        }

        public void codify() {
            summary = Main.codify(summary);
            description = Main.codify(description);
            specialConsiderations = Main.codify(specialConsiderations);
            assemblyInformation = Main.codify(assemblyInformation);
            resultCodes = Main.codify(resultCodes);
            seeAlso = Main.codify(seeAlso);
        }

        @Override
        public int compareTo(Entry o) {
            return funcName.compareTo(o.funcName);
        }
    }


    public static void paragraphify(String field, StringBuilder sb) {
        for (String para : field.split("\n\n")) {
            sb.append("<p>");
            sb.append(para);
            //sb.append("</p>");
        }
    }
    private static String codify(String text) {
        String[] lines = text.split("\n");
        StringBuilder out = new StringBuilder();
        for (int k = 0; k < lines.length; k++) {
            StringBuilder code = new StringBuilder();
            while (k < lines.length && isProbablyPascal(lines[k])) {
                code.append(lines[k]).append("\n");
                k++;
            }
            if (code.length() > 0) {
                out.append("<pre>").append(code).append("</pre>\n");
            }
            if (k < lines.length) {
                out.append(lines[k]).append("\n");
            }
        }
        String codified = out.toString();
        for (String symbol : symbols) {
            if (symbolInEntries(symbol)) {
                codified = codified.replace(" " + symbol + " ", " <code><a href=\"" + symbol + ".html\">" + symbol + "</a></code> ");
//            } else {
//                codified = codified.replace(symbol, "<code>" + symbol + "</code> ");
            }
        }
        return codified;
    }

    private static boolean symbolInEntries(String symbol) {
        for (Entry e : entries) {
            if (e.funcName.equals(symbol)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) throws IOException {
        PDDocument document = PDDocument.load(new File("essentials.pdf"));
        PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();

        processOutline(document, outline);

        Collections.sort(entries);

        FileWriter fos = new FileWriter("html/summary.html");
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body><ul>");
        for (Entry entry : entries) {
            sb.append("<li><a href=\"").append(entry.funcName).append(".html\" target=\"desc\">");
            sb.append(entry.funcName);
            sb.append("</li>");
        }
        sb.append("</ul></body></html>");
        fos.write(sb.toString());
        fos.flush();
        fos.close();

        for (Entry entry : entries) {
            entry.codify();
            sb = new StringBuilder();
            fos = new FileWriter("html/" + entry.funcName + ".html");
            sb.append("<!DOCTYPE html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body>");
            sb.append("<h1>");
            sb.append(entry.funcName);
            sb.append("</h1>");
            paragraphify(entry.summary, sb);
            sb.append("<h2>Description</h2>");
            paragraphify(entry.description, sb);
            sb.append("<h2>Special Considerations</h2>");
            paragraphify(entry.specialConsiderations, sb);
            sb.append("<h2>Assembly Language Info</h2>");
            paragraphify(entry.assemblyInformation, sb);
            sb.append("<h2>Result Codes</h2>");
            paragraphify(entry.resultCodes, sb);
            sb.append("<h2>See Also</h2>");
            paragraphify(entry.seeAlso, sb);
            sb.append("</body></html>");
            fos.write(sb.toString());
            fos.flush();
            fos.close();
        }
    }

    public static void processOutline(PDDocument document, PDOutlineNode bookmark) throws IOException {
        if (bookmark instanceof PDOutlineItem) {
            PDOutlineItem item = (PDOutlineItem) bookmark;
            if (item.getTitle().contains("Manager Routines")) {
            //if (parent instanceof PDOutlineItem && ((PDOutlineItem) parent).getTitle().contains("Routines")) {
                IMStreamEngine pts = new IMStreamEngine();
                pts.setDropThreshold(3.0f);
                pts.setParagraphEnd("\n\n");

                PDPageDestination start = document.getDocumentCatalog().findNamedDestinationPage(
                        (PDNamedDestination) item.getDestination());
                if (item.getNextSibling() != null) {
                    PDPageDestination end = document.getDocumentCatalog().findNamedDestinationPage(
                            (PDNamedDestination) item.getNextSibling().getDestination());
                    pts.setEndPage(end.retrievePageNumber());
                }

                pts.setStartPage(start.retrievePageNumber() + 1);

                String text = pts.getText(document);
                symbols.addAll(pts.getSymbols());
                String[] lines = text.split("\n");
                Entry currentEntry = new Entry();
                int state = SUMMARY;

                for (String line : lines) {
                    //System.out.println(line);
                    if (line.matches(".+\\d$")) {
                        if (line.contains(" ") && symbols.contains(line.substring(0, line.indexOf(' ')))) {
                            if (currentEntry != null) {
                                entries.add(currentEntry);
                            }
                            currentEntry = new Entry();
                            currentEntry.funcName = line.substring(0, line.indexOf(' '));
                            state = SUMMARY;
                        }
                    } else if ("DESCRIPTION".equals(line)) {
                        state = DESCRIPTION;
                    } else if ("SPECIAL CONSIDERATIONS".equals(line)) {
                        state = SPECIAL_CONSIDERATIONS;
                    } else if ("ASSEMBLY-LANGUAGE INFORMATION".equals(line)) {
                        state = ASSEMBLY_INFO;
                    } else if ("RESULT CODES".equals(line)) {
                        state = RESULT_CODES;
                    } else if ("SEE ALSO".equals(line)) {
                        state = SEE_ALSO;
                    } else if (!isJunkLine(line)) {
                        switch(state) {
                            case SUMMARY: currentEntry.summary += line + "\n"; break;
                            case DESCRIPTION: currentEntry.description += line + "\n"; break;
                            case SPECIAL_CONSIDERATIONS: currentEntry.specialConsiderations += line + "\n"; break;
                            case ASSEMBLY_INFO: currentEntry.assemblyInformation += line + "\n"; break;
                            case RESULT_CODES: currentEntry.resultCodes += line + "\n"; break;
                            case SEE_ALSO: currentEntry.seeAlso += line + "\n"; break;
                        }
                    }
                }

                //System.exit(0);
            }
        }

        PDOutlineItem child = bookmark.getFirstChild();
        while (child != null) {
            processOutline(document, child);
            child = child.getNextSibling();
        }
    }

    private static boolean isJunkLine(String line) {
        return line.startsWith("C H A P T E R") || line.endsWith(" 2")
                || line.endsWith(" Manager") || line.endsWith(" Reference") || line.matches(".+\\d-\\d+$");
    }

    private static boolean isProbablyPascal(String line) {
        return line.contains("{") || line.contains("}") || line.contains("=")
                || (line.contains(";") && line.contains(":"))
                || line.startsWith("FUNCTION")
                || line.startsWith("PROCEDURE")
                || line.toLowerCase().matches("function|procedure|var|integer|boolean|longint|const")
                || (line.split(" ").length == 1 && line.matches("^[a-z][A-Za-z0-9]+"));
    }

    private static final List<String> KEYWORDS = Arrays.asList("function", "procedure", "var", "integer", "boolean", "longint", "const");
}
