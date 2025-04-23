package com.nouah.revlo.generator;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.nouah.revlo.models.entity.Sales;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfReportGenerator {

    public static byte[] generateSalesReport(List<Sales> sales) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("Sales Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Table
        PdfPTable table = new PdfPTable(3); // 3 columns: Product, Quantity, Amount
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 2, 2});

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell h1 = new PdfPCell(new Phrase("Product", headFont));
        PdfPCell h2 = new PdfPCell(new Phrase("Quantity", headFont));
        PdfPCell h3 = new PdfPCell(new Phrase("Amount", headFont));

        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);

        for (Sales sale : sales) {
            PdfPCell productCell = new PdfPCell(new Phrase(String.valueOf(sale.getProducts())));
            PdfPCell quantityCell = new PdfPCell(new Phrase(String.valueOf(sale.getQuantity())));
            PdfPCell amountCell = new PdfPCell(new Phrase(String.format("%.2f", sale.getTotalAmount())));

            productCell.setPaddingLeft(4);
            quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            table.addCell(productCell);
            table.addCell(quantityCell);
            table.addCell(amountCell);
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
}
