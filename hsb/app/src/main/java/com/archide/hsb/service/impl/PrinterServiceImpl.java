package com.archide.hsb.service.impl;

import android.app.Activity;
import android.content.Context;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.FileUtils;
import com.hp.mss.hpprint.model.PDFPrintItem;
import com.hp.mss.hpprint.model.PrintItem;
import com.hp.mss.hpprint.model.PrintJobData;
import com.hp.mss.hpprint.model.asset.PDFAsset;
import com.hp.mss.hpprint.util.PrintUtil;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 20/04/17.
 */

public class PrinterServiceImpl {

    private KitchenDao kitchenDao;
    private Context context;

    public PrinterServiceImpl(Context context,boolean kitchen) {
        try {
            kitchenDao = new KitchenDaoImpl(context);
            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printKitchenOrders(String orderId,Activity activity) {
        try {
            KitchenOrdersListEntity kitchenOrdersListEntity = kitchenDao.getKitchenOrdersListEntity(orderId);
            List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities = kitchenDao.getKitchenOrderDetails(kitchenOrdersListEntity);
            String basedir = kitchenOrdersListEntity.getOrderId()+".pdf";

            String filePath = Utilities.getAppRootPath(context)+File.separator;

            filePath = filePath+basedir;

            generateKitchenPdf(kitchenOrdersListEntity,kitchenOrderDetailsEntities,filePath);
            PDFAsset pdfPath = new PDFAsset(filePath);
            PrintItem printItemDefault = new PDFPrintItem(PrintItem.ScaleType.CENTER, pdfPath);
            PrintJobData printJobData = new PrintJobData(activity, printItemDefault);
            printJobData.setJobName(orderId);
            PrintUtil.setPrintJobData(printJobData);
            PrintUtil.print(activity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void generateKitchenPdf(KitchenOrdersListEntity kitchenOrdersListEntity,List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities,String filePath)throws Exception{
        Document document = new Document(PageSize.A5);
        document.open();

        PdfWriter pdfWriter= PdfWriter.getInstance(document, new FileOutputStream(filePath));

        populateShopDetails(document);
        populateBillDetails(document,kitchenOrdersListEntity.getOrderId(),"Take Away",kitchenOrdersListEntity.getUserMobileNumber());

        Font normalBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

        PdfPTable myTable = new PdfPTable(4);
        myTable.setWidthPercentage(100);
        myTable.setSpacingBefore(0f);
        myTable.setSpacingAfter(0f);
        createBillHeader(myTable,normalBold);
        populateKitchenOrderData(kitchenOrderDetailsEntities,myTable);
        document.add(myTable);
        document.close();
    }


    private void populateKitchenOrderData(List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities,PdfPTable myTable){
        int count = 1;
        for(KitchenOrderDetailsEntity kitchenOrderDetailsEntity : kitchenOrderDetailsEntities){
            addEntry(myTable, String.valueOf(count));
            addEntry(myTable, kitchenOrderDetailsEntity.getName());
            addEntry(myTable, String.valueOf(kitchenOrderDetailsEntity.getQuantity()));
            addEntry(myTable, String.valueOf(kitchenOrderDetailsEntity.getQuantity()));
        }
    }


    private void createBillHeader(PdfPTable myTable, Font normalBold) {
        //"S.No"
        addEntry(myTable, "S.No");

        //SecondCell - itemName
        addEntry(myTable, "Item");

        // ThirdCell - Quantity
        addEntry(myTable, "Quantity");

        // FourthCell - Price
        addEntry(myTable, "Price");
    }

    private void addEntry(PdfPTable myTable,String text){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        myTable.addCell(cell);
    }


    private void populateShopDetails( Document document)throws Exception{
        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_CENTER);
        String[] shopDetails = {"NANDHANAM ARASAPPAR",""};
        for(String shopDetail : shopDetails){
            preface.add(new Phrase(shopDetail));
            preface.add(Chunk.NEWLINE);
        }

        document.add(preface);
    }

    private void populateBillDetails(Document document,String orderId,String tableNumber,String userMobile)throws Exception{
        addData(orderId,"10/04/2009",document);
        addData(tableNumber,"10/04/2009",document);
        addData(userMobile,"10/04/2009",document);


    }


    private void addData(String leftData,String rightData,Document document)throws Exception{
        Paragraph tablePdf = new Paragraph();
        tablePdf.setAlignment(Element.ALIGN_LEFT);
        tablePdf.add(new Phrase(leftData));
        document.add(tablePdf);

        Paragraph currentDatePdf = new Paragraph();
        currentDatePdf.setAlignment(Element.ALIGN_RIGHT);
        currentDatePdf.add(new Phrase(rightData));
        document.add(currentDatePdf);
        document.add(Chunk.NEWLINE);
    }



}
