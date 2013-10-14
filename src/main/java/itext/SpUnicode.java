package itext;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author the-ramones
 */
public class SpUnicode {

    public static final String TITLE = "Title of the document";
    public static final String SUB_TITLE = "User And Date";

    public static void main(String[] args) {
        build();
    }
    private static final Logger logger = Logger.getLogger(SpUnicode.class.getName());
    public static final String FONT = "files/cyberbit.ttf";
    static String FILE = "files/sp.pdf";
    /*
     * Fonts
     */
    static BaseFont bf;
    static Font titleFont;
    static Font titleOrangeFont;
    static Font subFont;
    static Font subOrangeFont;
    static Font midFont;
    static Font midBoldFont;
    static Font orangeFont;
    static Font smallBoldFont;
    static Font smallFont;
    static int[] margins = new int[]{36, 36, 54, 36};
    static float default_indent = 36.0f;
    /*
     * Messages
     */

    private static void build() {
        try {
            Document document = new Document(PageSize.A4,
                    margins[0], margins[1], margins[2], margins[3]);
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            createUnicodeFonts();
            addMetaData(document);
            addTitle(document);
            addContent(document);
            addFooter(document);
            document.close();
        } catch (DocumentException dex) {
            logger.warning("Didn't fill in the document");
            dex.printStackTrace();
        } catch (IOException ioex) {
            logger.warning("Didn't create base font for Unicode");
            ioex.printStackTrace();
        } catch (Exception e) {
            logger.warning("Didn't able to create PDF file due to exception");
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("Reports! Statistics iText PDF");
        document.addSubject("Statistics ");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addTitle(Document document) {
        // write a title
        try {
            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 1);
            Paragraph title = new Paragraph(TITLE, titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            preface.add(title);
            addEmptyLine(preface, 1);

            // write username and date sub-title
            Paragraph subTitle = new Paragraph(SUB_TITLE, subOrangeFont);
            subTitle.setAlignment(Paragraph.ALIGN_CENTER);
            preface.add(subTitle);
            addEmptyLine(preface, 3);
            document.add(preface);
        } catch (DocumentException dex) {
            logger.warning("Weren't able to write title to the document");
        }
    }

    private static void addContent(Document document) {
        try {
            Paragraph content = new Paragraph();
            // write inline statistics
            Paragraph statistics = new Paragraph();
            statistics.add(new Chunk("Count of performers", midFont));
            statistics.add(Chunk.NEWLINE);
            statistics.add(new Chunk("Count of activities", midFont));
            statistics.add(Chunk.NEWLINE);
            statistics.add(new Chunk("Average duration", midFont));
            statistics.add(Chunk.NEWLINE);
            content.add(statistics);
            addEmptyLine(content, 1);

            content.add(new Paragraph("Performers:", midBoldFont));

            Paragraph performers = new Paragraph(default_indent / 2);
            performers.setFont(midFont);
            performers.setIndentationLeft(default_indent);
            // write performers
            for (int i = 0; i < 10; i++) {
                performers.add(new Chunk("Performer", midFont));
                performers.add(Chunk.NEWLINE);
            }
            addEmptyLine(performers, 1);
            content.add(performers);

            content.add(new Paragraph("Activities:", midBoldFont));

            Paragraph activities = new Paragraph(default_indent / 2);
            activities.setFont(midFont);
            activities.setIndentationLeft(default_indent);
            // write performers
            for (int i = 0; i < 100; i++) {
                activities.add(new Chunk("Activity", midFont));
                activities.add(Chunk.NEWLINE);
            }
            addEmptyLine(activities, 1);
            content.add(activities);

            document.add(content);

            //document.newPage();
        } catch (DocumentException dex) {
            logger.warning("Don't able to write title of the document");
        }

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        PdfPCell c = new PdfPCell(new Phrase("Table Header 1"));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c);

        c = new PdfPCell(new Phrase("Table Header 2"));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c);

        c = new PdfPCell(new Phrase("Table Header 3"));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);
    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point element"));
        list.add(new ListItem("Second point element"));
        list.add(new ListItem("Third point element"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int count) {
        for (int i = 0; i < count; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void createUnicodeFonts() throws DocumentException, IOException {
        bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        titleFont = new Font(bf, 22, Font.BOLD);
        titleOrangeFont = new Font(bf, 22, Font.BOLD, new BaseColor(204, 103, 26));
        subFont = new Font(bf, 18, Font.BOLD);
        subOrangeFont = new Font(bf, 18, Font.BOLD, new BaseColor(204, 103, 26));
        midFont = new Font(bf, 14, Font.NORMAL);
        midBoldFont = new Font(bf, 14, Font.BOLD);
        orangeFont = new Font(bf, 12, Font.NORMAL, new BaseColor(204, 103, 26));
        smallBoldFont = new Font(bf, 12, Font.BOLD);
        smallFont = new Font(bf, 12, Font.NORMAL);
    }

    private static void addFooter(Document document) {
        try {
            Paragraph footer = new Paragraph("Paul Kulitski 2013 Reports!", orangeFont);
            footer.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(footer);
        } catch (DocumentException dex) {
            logger.warning("Cannot write a footer to the document");
        }
    }
}
