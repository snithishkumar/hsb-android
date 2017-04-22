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
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hsb.archide.com.hsb.R;

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

            String filePath = Utilities.getAppRootPath(activity)+File.separator;

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
        PdfWriter pdfWriter= PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();



        populateShopDetails(document);
        populateBillDetails(document,kitchenOrdersListEntity.getOrderId(),"Take Away",kitchenOrdersListEntity.getUserMobileNumber());

        Font normalBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

        PdfPTable myTable = new PdfPTable(4);
        myTable.setWidthPercentage(100);
        myTable.setSpacingBefore(0f);
        myTable.setSpacingAfter(0f);
        myTable.setWidths(new float[] { 1, 3, 1, 2 });
        createBillHeader(myTable,normalBold);
        populateKitchenOrderData(kitchenOrderDetailsEntities,myTable,document);
        footer(document);
        document.newPage();
        document.close();
    }


    private void populateKitchenOrderData(List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities,PdfPTable myTable,Document document)throws Exception{
        int count = 1;
       double subTotal = 0;
        for(KitchenOrderDetailsEntity kitchenOrderDetailsEntity : kitchenOrderDetailsEntities){
            addEntry(myTable, String.valueOf(count));
            addName(myTable, kitchenOrderDetailsEntity.getName());
            addEntry(myTable, String.valueOf(kitchenOrderDetailsEntity.getQuantity()));
            double rate = kitchenOrderDetailsEntity.getPrice() * kitchenOrderDetailsEntity.getQuantity();
            rate =  Utilities.roundOff(rate);
            subTotal += rate;
            addEntry(myTable, String.valueOf(rate));
        }

        document.add(myTable);
        LineSeparator ls = new LineSeparator();
        ls.setLineWidth(0.5f);
        document.add(new Chunk(ls));
        amountDetails(String.valueOf(subTotal),"0",String.valueOf(subTotal),document);
    }


    private void createBillHeader(PdfPTable myTable, Font normalBold) {
        //"S.No"
        addBillingHeading(myTable, "S.No");

        //SecondCell - itemName
        addBillingHeading(myTable, "Item");

        // ThirdCell - Quantity
        addBillingHeading(myTable, "Quantity");

        // FourthCell - Price
        addBillingHeading(myTable, "Price");

    }

    private void addBillingHeading(PdfPTable myTable,String text){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setPaddingBottom(10f);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        myTable.addCell(cell);
    }

    private void addEntry(PdfPTable myTable,String text){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        myTable.addCell(cell);
    }


    private void addName(PdfPTable myTable,String text){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(30f);
        myTable.addCell(cell);
    }


    private void amountDetails(String totalAmount,String tax, String subTotal,Document document)throws Exception{
        document.add(Chunk.NEWLINE);
        addAmountData(document,"Sub Total: ",subTotal);
        addAmountData(document,"Tax: ",tax);
        addAmountData(document,"Total: ",totalAmount);
    }

    private void footer(Document document)throws Exception{
        document.add(Chunk.NEWLINE);
        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_CENTER);
        preface.add(new Phrase("Thank you. Visit Again."));
        document.add(preface);
        document.add(Chunk.NEWLINE);
    }


    private void addAmountData(Document document,String label,String value)throws Exception{
        Paragraph p = new Paragraph(label);
        p.setAlignment(Element.ALIGN_RIGHT);
        p.add(Chunk.TABBING);
        p.add(value);
        p.setIndentationRight(35f);
        document.add(p);
    }


    private void populateShopDetails( Document document)throws Exception{
        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_CENTER);
        String[] shopDetails =  context.getResources().getStringArray(R.array.shop_details);
        for(String shopDetail : shopDetails){
            preface.add(new Phrase(shopDetail));
            preface.add(Chunk.NEWLINE);
        }

        document.add(preface);
        document.add(Chunk.NEWLINE);
    }

    private void populateBillDetails(Document document,String orderId,String tableNumber,String userMobile)throws Exception{
        Date date = new Date();
        addData("Order Id:"+orderId,"Date:"+getCurrentDate(date),document);
        addData("Table Bo:"+tableNumber,"Time"+getCurrentTime(date),document);
        document.add(Chunk.NEWLINE);
      //  addData("Mobile No:"+userMobile,"   ",document);


    }

    private String getCurrentDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
       return simpleDateFormat.format(date);
    }


    private String getCurrentTime(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm Z");
        return simpleDateFormat.format(date);
    }





    private void addData(String leftData,String rightData,Document document)throws Exception{
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph(leftData);
        p.add(new Chunk(glue));
        p.add(rightData);


        document.add(p);

    }



}
