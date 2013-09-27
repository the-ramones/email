package pdf;

import java.io.IOException;
import org.apache.pdfbox.examples.pdmodel.AddImageToPDF;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Apache PDFBox tryout
 * 
 * @author paul
 */
public class PDFCreator {

    public static void main(String[] args) throws IOException, COSVisitorException {
        createPDF();
    }
    
    private static void createPDF() throws IOException, COSVisitorException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        
        PDFont font = PDType1Font.HELVETICA;
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        contentStream.beginText();
        
        contentStream.endText();
        
        document.addPage(page);
        document.addPage(new PDPage());
        document.addPage(new PDPage());
        
        document.save("files/stats.pdf");
        
        document.close();
    }
}
