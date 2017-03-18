package com.archide.hsb.service.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;

import com.archide.hsb.util.FileUtils;
import com.hp.mss.hpprint.model.ImagePrintItem;
import com.hp.mss.hpprint.model.PDFPrintItem;
import com.hp.mss.hpprint.model.PrintItem;
import com.hp.mss.hpprint.model.PrintJobData;
import com.hp.mss.hpprint.model.asset.PDFAsset;
import com.hp.mss.hpprint.util.PrintUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nithish on 18/03/17.
 */

public class PrinterServiceImpl {

    private KitchenDao kitchenDao;
    private Context context;

    public PrinterServiceImpl(Context context) {
        try {
            kitchenDao = new KitchenDaoImpl(context);
            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printOrders(String orderId,Activity activity) {
        try {
            KitchenOrdersListEntity kitchenOrdersListEntity = kitchenDao.getKitchenOrdersListEntity(orderId);
            List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities = kitchenDao.getKitchenOrderDetails(kitchenOrdersListEntity);
            String filePath = generatePDF(kitchenOrdersListEntity,kitchenOrderDetailsEntities);
            PDFAsset pdfPath = new PDFAsset(filePath);
            PrintItem printItemDefault = new PDFPrintItem(PrintItem.ScaleType.CENTER, pdfPath);
            PrintJobData printJobData = new PrintJobData(activity, printItemDefault);
            printJobData.setJobName("Example");
            PrintUtil.setPrintJobData(printJobData);
            PrintUtil.print(activity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String generatePDF(KitchenOrdersListEntity  kitchenOrdersListEntity,List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities) {
        //String FILE = Environment.getExternalStorageDirectory().toString() + "/sdcard/" + cottonBookListEntity.getCottonBookId()+".pdf";
        String basedir = kitchenOrdersListEntity.getOrderId()+".pdf";
        String filePath = FileUtils.getAppRootPath(context)+File.separator;
        File myDir = new File(filePath);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Document document = new Document(PageSize.A5);
        filePath =filePath+basedir;
        try {
            Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD
                    | Font.UNDERLINE, BaseColor.GRAY);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
            Font normalBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            PdfWriter pdfWriter=PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            PdfPTable myTable = new PdfPTable(4);
            myTable.setWidthPercentage(100);
            myTable.setSpacingBefore(0f);
            myTable.setSpacingAfter(0f);
            PdfContentByte cb = pdfWriter.getDirectContent();
        //  createPdfHeader(myTable,normalBold);
            int count=0;
            for (KitchenOrderDetailsEntity kitchenOrderDetailsEntity : kitchenOrderDetailsEntities) {
                // FirstCell- id
                count++;
                PdfPCell cell = new PdfPCell(new Phrase(""+count,smallBold));
                cell.setFixedHeight(15);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(2);
                myTable.addCell(cell);
                //SecondCell - itemName
                cell = new PdfPCell(new Phrase(kitchenOrderDetailsEntity.getName(),smallBold));
                cell.setFixedHeight(15);
                cell.setBorder(Rectangle.NO_BORDER);
                myTable.addCell(cell);
                //ThirdCell - quantity
                cell = new PdfPCell(new Phrase(""+kitchenOrderDetailsEntity.getQuantity(),smallBold));
                cell.setFixedHeight(15);
                cell.setBorder(Rectangle.NO_BORDER);
                myTable.addCell(cell);
                //FourthCell - Price
                cell = new PdfPCell(new Phrase(""+kitchenOrderDetailsEntity.getQuantity(),smallBold));
                cell.setFixedHeight(15);
                cell.setBorder(Rectangle.NO_BORDER);
                myTable.addCell(cell);
            }
            document.add(myTable);
            document.newPage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        return filePath;
    }
    public void createPdfHeader(PdfPTable myTable,Font normalBold)
    {
        PdfPCell cell = new PdfPCell(new Phrase("S.No"));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        myTable.addCell(cell);
        //SecondCell - itemName
        cell = new PdfPCell(new Phrase("Item",normalBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        myTable.addCell(cell);
        //ThirdCell - quantity
        cell = new PdfPCell(new Phrase("Quantity",normalBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        myTable.addCell(cell);
        //FourthCell - Price
        cell = new PdfPCell(new Phrase("Price",normalBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        myTable.addCell(cell);
    }
}
