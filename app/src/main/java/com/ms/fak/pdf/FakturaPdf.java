package com.ms.fak.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Faktura;
import com.ms.fak.entities.Global;
import com.ms.fak.entities.Komitent;
import com.ms.fak.entities.Stavka;
import com.ms.fak.utl.AppUtl;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class FakturaPdf {
    private final Global glob;
    private final Faktura faktura;
    private final Document document;
    private final FileOutputStream outStream;
    private final String DIVA = "assets/BROADW.TTF";
    private final Font font12 = FontFactory.getFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.NOT_EMBEDDED, 12f, Font.NORMAL, BaseColor.BLACK);
    private final Font font16 = FontFactory.getFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.NOT_EMBEDDED, 16f, Font.NORMAL, BaseColor.BLACK);
    private final Font fontDiva = FontFactory.getFont(DIVA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 24f, Font.NORMAL, BaseColor.BLACK);

    public FakturaPdf(Faktura faktura, FileOutputStream outStream) throws DocumentException {
        this.faktura = faktura;
        this.outStream = outStream;

        glob = Api.getInstance().getGlobal();
        document = new Document();
        document.setMargins(72f, 40f, 72f, 72f);

        PdfWriter.getInstance(document, outStream);

        document.open();

        addMetaData();
        addMemorandum();
        addKomitentAndRacun();
        addStavke();

        document.close();
    }

    private void addMetaData() {
        document.addTitle("Faktura br. " + faktura.getBroj());
        document.addSubject("Fakturisanje");
        document.addKeywords("Faktura");
        document.addAuthor(glob.getFirmaNaziv1());
        document.addCreator(glob.getFirmaNaziv2());
    }

    private void addMemorandum() throws DocumentException {

        PdfPTable table;

        table = new PdfPTable(new float[]{0.9f, 0.6f, 0.6f});
        table.setWidthPercentage(100.0f);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table.getDefaultCell().setBorderWidthBottom(2f);
        table.getDefaultCell().setBorderWidthTop(2f);
        table.getDefaultCell().disableBorderSide(Rectangle.LEFT);
        table.getDefaultCell().disableBorderSide(Rectangle.RIGHT);
        table.setSpacingAfter(32f);

        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100.0f);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //table1.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        Paragraph para;

        para = new Paragraph();
        para.setFont(font12);
        para.setAlignment(Element.ALIGN_CENTER);
        para.setSpacingAfter(8f);
        para.add(glob.getFirmaNaziv1());
        table1.addCell(para);

        para = new Paragraph();
        para.setFont(fontDiva);
        para.setAlignment(Element.ALIGN_CENTER);
        para.setSpacingAfter(4f);
        para.add(glob.getFirmaNaziv2());
        table1.addCell(para);

        table.addCell(table1);

        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100.0f);
        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //table2.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table2.setSpacingAfter(4f);

        table2.addCell(new Phrase(glob.getFirmaMesto(), font12));
        table2.addCell(new Phrase(glob.getFirmaAdresa(), font12));
        table2.addCell(new Phrase(glob.getFirmaFmtTel(), font12));
        table2.addCell(new Phrase(glob.getFirmaFmtFax(), font12));
        table.addCell(table2);

        table2 = new PdfPTable(1);
        table2.setWidthPercentage(100.0f);
        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //table2.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table2.setSpacingAfter(4f);

        table2.addCell(new Phrase(glob.getFirmaFmtPib(), font12));
        table2.addCell(new Phrase(glob.getFirmaFmtMbr(), font12));
        table2.addCell(new Phrase(glob.getFirmaFmtDel(), font12));
        table2.addCell(new Phrase(glob.getFirmaFmtZiro(), font12));
        table.addCell(table2);

        document.add(table);
    }

    private void addKomitentAndRacun() throws DocumentException {

        PdfPTable table;

        table = new PdfPTable(new float[]{1.2f, 0.2f, 0.8f});
        table.setWidthPercentage(100.0f);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table.getDefaultCell().setBorderWidthBottom(2f);
        table.getDefaultCell().setBorderWidthTop(2f);
        table.getDefaultCell().disableBorderSide(Rectangle.LEFT);
        table.getDefaultCell().disableBorderSide(Rectangle.RIGHT);
        table.setSpacingAfter(32f);

        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100.0f);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //table1.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table1.setSpacingAfter(4f);

        Komitent komitent = Api.getInstance().getKomitentById(faktura.getKomitentId());

        table1.addCell(new Phrase(komitent.getNaziv(), font16));
        table1.addCell(new Phrase(komitent.getMesto(), font12));
        table1.addCell(new Phrase(komitent.getAdresa(), font12));
        table1.addCell(new Phrase("PIB " + komitent.getPib(), font12));
        table.addCell(table1);

        PdfPCell empty = new PdfPCell(new Phrase(" "));
        empty.setBorder(PdfPCell.NO_BORDER);
        table.addCell(empty);

        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100.0f);
        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //table2.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table2.setSpacingAfter(4f);

        SimpleDateFormat sdf = new SimpleDateFormat(AppUtl.fmtDate);

        table2.addCell(new Phrase("Račun br. " + faktura.getBroj(), font16));
        table2.addCell(new Phrase(faktura.getMesto() + ", " + sdf.format(faktura.getDatum()), font12));
        table2.addCell(new Phrase(" "));
        table2.addCell(new Phrase("Datum prometa usluga " + sdf.format(faktura.getDatumPrometa()), font12));
        table.addCell(table2);

        document.add(table);
    }

    private void addStavke() throws DocumentException {

        PdfPTable table;

        table = new PdfPTable(new float[]{0.2f, 1.4f, 0.6f});
        table.setWidthPercentage(100.0f);
        //table.setSpacingAfter(16f);

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("R. b.", font12));
        //cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell.setBorderWidthBottom(2f);
        cell.setUseBorderPadding(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Opis usluge", font12));
        //cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5f);
        //cell.setBorderWidthBottom(2f);
        cell.setUseBorderPadding(true);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Iznos", font12));
        //cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5f);
        //cell.setBorderWidthBottom(2f);
        cell.setUseBorderPadding(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        table.setHeaderRows(1);

        Integer n = 1;
        BigDecimal ukupno = new BigDecimal(0);

        for (Stavka stavka : faktura.getStavkaCollection()) {
            cell = new PdfPCell(new Phrase(n.toString(), font12));
            //cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(stavka.getOpis(), font12));
            //cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5f);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(AppUtl.fmtIznos.format(stavka.getIznos()), font12));
            //cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            n++;
            ukupno = ukupno.add(stavka.getIznos());
        }
        cell = new PdfPCell(new Phrase(" ", font12));
        //cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5f);
        //cell.setBorderWidthTop(2f);
        cell.setUseBorderPadding(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Ukupno: ", font12));
        //cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5f);
        //cell.setBorderWidthTop(2f);
        cell.setUseBorderPadding(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(AppUtl.fmtIznos.format(ukupno), font12));
        //cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5f);
        //cell.setBorderWidthTop(2f);
        cell.setUseBorderPadding(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        document.add(table);

        Paragraph para;

        para = new Paragraph("Slovima ("+AppUtl.iznosSlovima(ukupno)+")", font12);
        para.setAlignment(Element.ALIGN_RIGHT);
        para.setSpacingAfter(16f);

        document.add(para);

        para = new Paragraph("Plaćanje: " + faktura.getNacinPlacanja(), font12);
        para.setAlignment(Element.ALIGN_LEFT);
        //para.setSpacingAfter(16f);

        document.add(para);

        para = new Paragraph(faktura.getNapomenaPdv(), font12);
        para.setAlignment(Element.ALIGN_LEFT);
        para.setSpacingAfter(24f);

        document.add(para);

        para = new Paragraph("MP", font12);
        para.setAlignment(Element.ALIGN_CENTER);
        para.setSpacingAfter(24f);

        document.add(para);

        para = new Paragraph("_____________________________", font12);
        para.setAlignment(Element.ALIGN_RIGHT);

        document.add(para);
    }

}
