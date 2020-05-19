package com.converter.promob.xml.service;

import com.converter.promob.xml.model.Item;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class XlsService {

    public String generateXlsFile(List<Item> itens) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("items");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 9);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        Row headerRow = sheet.createRow(0);

        Gson g = new Gson();

        return g.toJson(itens);



//        String[] headers = {"MODULO", "CLIENTE - AMBIENTE","DESC. PEÇA","COMP", "LARG","QUANT","BORDA SUP","BORDA INF","BORDA DIR","BORDA ESQ","COR DA FITA DE BORDA","CHAPA","ESP."};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//
//            if(headers[i].equals("COMP") || headers[i].equals("BORDA SUP") || headers[i].equals("BORDA INF")){
//                CellStyle cellStyle = setRedColor(workbook);
//                cellStyle.setFont(headerFont);
//                cell.setCellStyle(cellStyle);
//            }
//            else if(headers[i].equals("LARG") || headers[i].equals("BORDA DIR") || headers[i].equals("BORDA ESQ")){
//                CellStyle cellStyle = setBlueColor(workbook);
//                cellStyle.setFont(headerFont);
//                cell.setCellStyle(cellStyle);
//            } else{
//                CellStyle cellStyle = setDefaultStyle(workbook);
//                cellStyle.setFont(headerFont);
//                cell.setCellStyle(cellStyle);
//            }
//
//        }
//
//        int rowNum = 1;
//
//        for (Item item : itens) {
//            if(item.isComponent()){
//                Row row = sheet.createRow(rowNum);
//                int initialRow = rowNum;
//                Cell cellModel = row.createCell(0);
//                cellModel.setCellValue(item.getDescription() + " - " + item.getUniqueId() + " - " +item.getTextDimension());
//                cellModel.setCellStyle(setModuleStyle(workbook));
//
//                Cell; cellAmb = row.createCell(1);
//                cellAmb.setCellValue(item.getAmbiente());
//                cellAmb.setCellStyle(setDefaultStyle(workbook));
//
//                Cell cellDesc = row.createCell(2);
//
//                cellDesc.setCellValue(item.getDescription() + " - " + item.getUniqueId());
//
//                cellDesc.setCellStyle(setDefaultStyle(workbook));
//
//                buildRow(item,row,workbook);
//                rowNum++;
//                if(!item.getChildItems().isEmpty()){
//                    for (Item filho : item.getChildItems()) {
//                        Row childRow = sheet.createRow(rowNum);
//
//                        Cell cellModelC = row.createCell(0);
//                        cellModelC.setCellValue("");
//                        cellModelC.setCellStyle(setDefaultStyle(workbook));
//
//                        Cell cellAmbC = childRow.createCell(1);
//                        cellAmbC.setCellValue(filho.getAmbiente());
//                        cellAmbC.setCellStyle(setDefaultStyle(workbook));
//
//                        Cell cellDescC = childRow.createCell(2);
//                        cellDescC.setCellValue(filho.getDescription() + " - " + item.getUniqueId());
//                        cellDescC.setCellStyle(setDefaultStyle(workbook));
//
//                        buildRow(filho, childRow,workbook);
//
//                        rowNum++;
//                    }
//                }
//
//                int finalRow = rowNum;
//                if(finalRow > initialRow+1){
//                    sheet.addMergedRegion(new CellRangeAddress(initialRow,finalRow-1,0,0));
//                }
//
//                rowNum++;
//            } else if(!item.getChildItems().isEmpty()) {
//                int initialRow = rowNum;
//                for (Item filho : item.getChildItems()) {
//
//                    Row row = sheet.createRow(rowNum);
//
//                    Cell cellModel = row.createCell(0);
//                    cellModel.setCellValue(item.getDescription() + " - " + item.getUniqueId() + " - " +item.getTextDimension());
//                    cellModel.setCellStyle(setModuleStyle(workbook));
//
//
//                    Cell cellAmb = row.createCell(1);
//                    cellAmb.setCellValue(filho.getAmbiente());
//                    cellAmb.setCellStyle(setDefaultStyle(workbook));
//
//                    Cell cellDesc = row.createCell(2);
//                    cellDesc.setCellValue(filho.getDescription() + " - " + item.getUniqueId());
//                    cellDesc.setCellStyle(setDefaultStyle(workbook));
//
//                    buildRow(filho, row,workbook);
//
//                    rowNum++;
//                }
//                int finalRow = rowNum;
//                if(finalRow > initialRow+1){
//                    sheet.addMergedRegion(new CellRangeAddress(initialRow,finalRow-1,0,0));
//                }
//                rowNum++;
//            }
//        }
//
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        workbook.write(out);
//
//        out.close();
//        return out;
    }


    private void buildRow(Item filho, Row row,Workbook workbook) {
        Cell cellComp = row.createCell(3);
        cellComp.setCellValue(filho.getComp());
        cellComp.setCellStyle(setRedColor(workbook));

        Cell largCell = row.createCell(4);
        largCell.setCellValue(filho.getLarg());
        largCell.setCellStyle(setBlueColor(workbook));

        Cell cellQuant = row.createCell(5);
        cellQuant.setCellValue(filho.getQuant());
        cellQuant.setCellStyle(setDefaultStyle(workbook));

        Cell bordSeupCell = row.createCell(6);
        bordSeupCell.setCellValue(filho.getBord_sup());
        bordSeupCell.setCellStyle(setRedColor(workbook));

        Cell bordInfCell = row.createCell(7);
        bordInfCell.setCellValue(filho.getBord_inf());
        bordInfCell.setCellStyle(setRedColor(workbook));

        Cell bordEsqCell = row.createCell(8);
        bordEsqCell.setCellValue(filho.getBord_esq());
        bordEsqCell.setCellStyle(setBlueColor(workbook));

        Cell bordDirCell = row.createCell(9);
        bordDirCell.setCellValue(filho.getBord_dir());
        bordDirCell.setCellStyle(setBlueColor(workbook));

        Cell cor_fita = row.createCell(10);
        cor_fita.setCellValue(filho.getCor_borda());
        cor_fita.setCellStyle(setDefaultStyle(workbook));

        Cell cellChapa = row.createCell(11);
        cellChapa.setCellValue(filho.getChapa());
        cellChapa.setCellStyle(setDefaultStyle(workbook));

        Cell cellEsp = row.createCell(12);
        cellEsp.setCellValue(filho.getEsp());
        cellEsp.setCellStyle(setDefaultStyle(workbook));
    }


    private CellStyle setModuleStyle(Workbook workbook){

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 9);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle style = workbook.createCellStyle();

        getAllBord(style);

        style.setFont(headerFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle setDefaultStyle(Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        getAllBord(style);
        return style;
    }

    private CellStyle setBlueColor(Workbook workbook) {
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFPalette palette = hwb.getCustomPalette();
        HSSFColor myColor = palette.findSimilarColor(189, 214, 238);
        CellStyle style = workbook.createCellStyle();
        getAllBord(style);
        style.setFillForegroundColor(myColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private  CellStyle getAllBord(CellStyle style){
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private CellStyle setRedColor(Workbook workbook ) {

        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFPalette palette = hwb.getCustomPalette();

        HSSFColor myColor = palette.findSimilarColor(247, 202, 172);

        CellStyle style = workbook.createCellStyle();
        getAllBord(style);
        style.setFillForegroundColor(myColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

}
