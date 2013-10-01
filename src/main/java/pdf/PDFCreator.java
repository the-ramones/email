package pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.encoding.Encoding;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Apache PDFBox tryout
 *
 * @author paul
 */
public class PDFCreator {

    /*
     * Tryouts
     */
    public static void main(String[] args) throws IOException, COSVisitorException {
        createPDF();
        //createAppendablePDF();
    }
    /*
     * Statistic data
     */
    static String user = "Paul Daniel";
    static Date date = new Date();
    static String performers = "dan van, man can, sam tam,dan van, man can, sam tam,d van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam,dan van, man can, sam tam";
    static String activities = "dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping, dance, drinking, face hilling, pit stopping";
    /*
     * Constants
     *   100 - top margin
     *   100 - bottop margin
     *   50 - left margin
     *   50 - right margin
     */
    static float verticalDistance = 800f;
    static float horizontalDistance = 600f;
    static float bottomUpMargin = 80f;
    static float leftRightMargin = 50f;
    static float bottomUpDistance = verticalDistance - 2 * bottomUpMargin;
    static float leftRightDistance = horizontalDistance - 2 * leftRightMargin;
    /*
     * Fonts
     */
    static PDFont helveticaBold = PDType1Font.HELVETICA_BOLD;
    static PDFont helvetica = PDType1Font.HELVETICA;
    static PDDocument document;

    static {
        try {
            document = new PDDocument();
        } catch (IOException ex) {
            Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    static float vertPos = 0;
    static float horPos = 0;

    private static void createPDF() throws IOException, COSVisitorException {

        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        vertPos = 0;
        contentStream.beginText();
        contentStream.setFont(helveticaBold, 16);

        contentStream.moveTextPositionByAmount(185, 700);
        contentStream.drawString("Reports! checklist statistics");

        contentStream.setFont(helveticaBold, 14);
        contentStream.moveTextPositionByAmount(-15, -30);
        contentStream.drawString(user + ", " + getFormattedDate());

        contentStream.setFont(helvetica, 14);
        contentStream.moveTextPositionByAmount(-120, -40);
        contentStream.drawString("Amount of distinct performers: " + 12L);
        contentStream.moveTextPositionByAmount(0, -32);
        contentStream.drawString("Amount of distinct activities: " + 8L);
        contentStream.moveTextPositionByAmount(0, -32);
        contentStream.drawString("Average activity duration: " + 15.4 + " day(s)");

        contentStream.setFont(helveticaBold, 14);
        contentStream.moveTextPositionByAmount(0, -40);
        contentStream.drawString("Performers: ");
        contentStream.moveTextPositionByAmount(0, -10);

        float margin = getFontHeight(helvetica, 12);

        contentStream.setFont(helvetica, 12);
        contentStream.moveTextPositionByAmount(20, 0);
        for (String performer : performers.split(", ")) {
            contentStream.moveTextPositionByAmount(0, -margin);
            contentStream.drawString(performer);
        }

        contentStream.moveTextPositionByAmount(-20, -40);
        contentStream.setFont(helveticaBold, 14);
        contentStream.drawString("Activities: ");
        contentStream.moveTextPositionByAmount(0, -10);

        contentStream.setFont(helvetica, 12);
        contentStream.moveTextPositionByAmount(20, 0);
        for (String activity : activities.split(", ")) {
            contentStream.moveTextPositionByAmount(0, -margin);
            contentStream.drawString(activity);
        }

        contentStream.moveTextPositionByAmount(175, -500);
        contentStream.drawString("Reports! 2013, Paul Kulitski");

        contentStream.endText();

        contentStream.close();


        document.save("files/stats2.pdf");

        document.close();
    }

    private static void createAppendablePDF() throws IOException, COSVisitorException {

        PDPageContentStream stream =
                appendStringLine(null, "Reports! checklist statistics",
                75, 0, helveticaBold, 16);

        appendStringLine(stream, user + ", " + getFormattedDate(),
                -15, -30, helveticaBold, 14);


        appendStringLine(stream, "Amount of distinct performers: " + 12L,
                -120, -40, helvetica, 14);

        appendStringLine(stream, "Amount of distinct activities: " + 8L,
                0, -32, helvetica, 14);

        appendStringLine(stream, "Average activity duration: " + 15.4 + " day(s)",
                0, -32, helvetica, 14);

        // append list of performers
        appendStringLine(stream, "Performers: ", 0, -40, helveticaBold, 14);
        stream.moveTextPositionByAmount(20, 0);
        horPos += 20;

        float margin = getFontHeight(helvetica, 12);
        for (String performer : performers.split(", ")) {
            stream = appendStringLine(stream, performer, 0, -margin, helvetica, 12);
        }

        // append list of activities
        appendStringLine(stream, "Activities: ", -20, -40, helveticaBold, 14);
        stream.moveTextPositionByAmount(20, 0);
        horPos += 20;

        for (String activity : activities.split(", ")) {
            stream = appendStringLine(stream, activity, 0, -margin, helvetica, 12);
        }

        appendFooter(stream, "Reports! 2013, Paul Kulitski", 175, -500,
                helvetica, 12);
        stream.endText();
        stream.close();

        document.save("files/stats3.pdf");
        document.close();
    }

    static PDPageContentStream appendFooter(PDPageContentStream stream,
            String line, float indent, float margin,
            PDFont font, float fontSize) throws IOException {
        if (stream != null) {
            float width = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
            float pWidth = width * line.length();
            stream.moveTextPositionByAmount(leftRightMargin - horPos, leftRightMargin - vertPos);
            stream.setFont(font, fontSize);
            stream.drawString(line);
        }
        return stream;
    }

    static PDPageContentStream appendStringLine(PDPageContentStream stream,
            String line, float indent, float margin,
            PDFont font, float fontSize) throws IOException {
        PDPageContentStream contentStream = stream;
        System.out.println("VER.POS: " + vertPos);
        System.out.println("HOR.POS: " + horPos);
        System.out.println("LINE: " + line);
        /*
         * Firts page
         */
        if (contentStream == null) {
            System.out.println("NULL STREAM");
            PDPage newPage = new PDPage();
            document.addPage(newPage);
            contentStream = new PDPageContentStream(document, newPage);
            contentStream.beginText();
            vertPos = verticalDistance - bottomUpMargin + margin;
            horPos = leftRightMargin + indent;
            contentStream.moveTextPositionByAmount(horPos, vertPos);

            System.out.println("VER.POS: " + vertPos);
            System.out.println("HOR.POS: " + horPos);
        }
        //
        if ((vertPos + margin) > bottomUpMargin) {
            // CURRENT PAGE
            System.out.println("OLD STREAM");
            vertPos += margin;
            horPos += indent;
            contentStream.moveTextPositionByAmount(indent, margin);
            contentStream.setFont(font, fontSize);
            contentStream.drawString(line);
            stream = contentStream;
            return stream;
        } else {
            System.out.println("NEW STREAM");
            // NEXT PAGE
            stream.endText();
            stream.close();

            PDPage newPage = new PDPage();
            document.addPage(newPage);
            PDPageContentStream newStream = new PDPageContentStream(document, newPage);

            newStream.beginText();
            vertPos = verticalDistance - bottomUpMargin;
            horPos = horPos + indent;

            newStream.moveTextPositionByAmount(horPos + indent, vertPos);

            newStream.setFont(font, fontSize);

            newStream.drawString(line);

            stream = newStream;
            return stream;
        }
    }

    private static PDPageContentStream checkPageBreak(float pos) {
        return null;
    }

    private static void printMultipleLines(PDPageContentStream contentStream,
            PDFont font, List<String> lines, float x, float y) throws IOException {
        if (lines.size() == 0) {
            return;
        }
        final int numberOfLines = lines.size();
        final float fontHeight = getFontHeight(font, 12);

        contentStream.beginText();
        contentStream.appendRawCommands(fontHeight + " TL\n");
        contentStream.moveTextPositionByAmount(x, y);
        contentStream.drawString(lines.get(0));
        for (int i = 1; i < numberOfLines; i++) {
            contentStream.appendRawCommands(escapeString(lines.get(i)));
            contentStream.appendRawCommands(" \'\n");
        }
        contentStream.endText();
    }

    private static String escapeString(String text) throws IOException {
        try {
            COSString string = new COSString(text);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            string.writePDF(buffer);
            return new String(buffer.toByteArray(), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // every JVM must know ISO-8859-1
            throw new RuntimeException(e);
        }
    }

    private static float getFontHeight(PDFont font, float size) {
        float fontHeight =
                font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * size;
        return fontHeight;
    }

    private static String getFormattedDate() {
        DateFormat format = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
        return format.format(new Date());
    }
}
