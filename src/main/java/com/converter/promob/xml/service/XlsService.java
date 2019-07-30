package com.converter.promob.xml.service;

import com.converter.promob.xml.model.Item;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class XlsService {

    public ByteArrayOutputStream generateXlsFile(List<Item> itens) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("items");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 9);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        Row headerRow = sheet.createRow(0);

        String[] headers = {"CLIENTE - AMBIENTE","DESC. PEÇA","COMP", "LARG","QUANT","BORDA SUP","BORDA INF","BORDA DIR","BORDA ESQ","COR DA FITA DE BORDA","CHAPA","ESP."};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);

            if(headers[i].equals("COMP") || headers[i].equals("BORDA SUP") || headers[i].equals("BORDA INF")){
                CellStyle cellStyle = setRedColor(workbook);
                cellStyle.setFont(headerFont);
                cell.setCellStyle(cellStyle);
            }
            else if(headers[i].equals("LARG") || headers[i].equals("BORDA DIR") || headers[i].equals("BORDA ESQ")){
                CellStyle cellStyle = setBlueColor(workbook);
                cellStyle.setFont(headerFont);
                cell.setCellStyle(cellStyle);
            } else{
                CellStyle cellStyle = setDefaultStyle(workbook);
                cellStyle.setFont(headerFont);
                cell.setCellStyle(cellStyle);
            }

        }

        int rowNum = 1;

        for (Item item : itens) {
            if(item.isComponent()){
                Row row = sheet.createRow(rowNum);

                Cell cellAmb = row.createCell(0);
                cellAmb.setCellValue(item.getAmbiente());
                cellAmb.setCellStyle(setDefaultStyle(workbook));

                Cell cellDesc = row.createCell(1);

                cellDesc.setCellValue(item.getDescription() + " - " + item.getUniqueId());

                cellDesc.setCellStyle(setDefaultStyle(workbook));

                buildRow(item,row,workbook);
                rowNum++;
                if(!item.getChildItems().isEmpty()){
                    for (Item filho : item.getChildItems()) {
                        Row childRow = sheet.createRow(rowNum);

                        Cell cellAmbC = childRow.createCell(0);
                        cellAmbC.setCellValue(filho.getAmbiente());
                        cellAmbC.setCellStyle(setDefaultStyle(workbook));

                        Cell cellDescC = childRow.createCell(1);
                        cellDescC.setCellValue(filho.getDescription() + " - " + item.getUniqueId());
                        cellDescC.setCellStyle(setDefaultStyle(workbook));

                        buildRow(filho, childRow,workbook);

                        rowNum++;
                    }
                }

                
                rowNum++;
            } else if(!item.getChildItems().isEmpty()) {
                for (Item filho : item.getChildItems()) {
                    Row row = sheet.createRow(rowNum);

                    Cell cellAmb = row.createCell(0);
                    cellAmb.setCellValue(filho.getAmbiente());
                    cellAmb.setCellStyle(setDefaultStyle(workbook));

                    Cell cellDesc = row.createCell(1);
                    cellDesc.setCellValue(filho.getDescription() + " - " + item.getUniqueId());
                    cellDesc.setCellStyle(setDefaultStyle(workbook));

                    buildRow(filho, row,workbook);

                    rowNum++;
                }
                rowNum++;
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        out.close();
        return out;
    }


    private void buildRow(Item filho, Row row,Workbook workbook) {
        Cell cellComp = row.createCell(2);
        cellComp.setCellValue(filho.getComp());
        cellComp.setCellStyle(setRedColor(workbook));

        Cell largCell = row.createCell(3);
        largCell.setCellValue(filho.getLarg());
        largCell.setCellStyle(setBlueColor(workbook));

        Cell cellQuant = row.createCell(4);
        cellQuant.setCellValue(filho.getQuant());
        cellQuant.setCellStyle(setDefaultStyle(workbook));

        Cell bordSeupCell = row.createCell(5);
        bordSeupCell.setCellValue(filho.getBord_sup());
        bordSeupCell.setCellStyle(setRedColor(workbook));

        Cell bordInfCell = row.createCell(6);
        bordInfCell.setCellValue(filho.getBord_inf());
        bordInfCell.setCellStyle(setRedColor(workbook));

        Cell bordEsqCell = row.createCell(7);
        bordEsqCell.setCellValue(filho.getBord_esq());
        bordEsqCell.setCellStyle(setBlueColor(workbook));

        Cell bordDirCell = row.createCell(8);
        bordDirCell.setCellValue(filho.getBord_dir());
        bordDirCell.setCellStyle(setBlueColor(workbook));

        Cell cor_fita = row.createCell(9);
        cor_fita.setCellValue(filho.getCor_borda());
        cor_fita.setCellStyle(setDefaultStyle(workbook));

        Cell cellChapa = row.createCell(10);
        cellChapa.setCellValue(filho.getChapa());
        cellChapa.setCellStyle(setDefaultStyle(workbook));

        Cell cellEsp = row.createCell(11);
        cellEsp.setCellValue(filho.getEsp());
        cellEsp.setCellStyle(setDefaultStyle(workbook));
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
