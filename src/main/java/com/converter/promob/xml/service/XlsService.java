package com.converter.promob.xml.service;

import com.converter.promob.xml.model.Item;
import org.apache.poi.ss.usermodel.*;
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

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        String[] headers = {"CLIENTE - AMBIENTE","DESC. PEÇA","COMP", "LARG","QUANT","MATERIAL","BORDA SUP","BORDA INF","BORDA DIR","BORDA ESQ","VEIO","CHAPA","ESP."};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;

        for (Item item : itens) {
            if(!item.getChildItems().isEmpty()) {
                for (Item filho : item.getChildItems()) {
                    Row row = sheet.createRow(rowNum);
                    row.createCell(0).setCellValue(filho.getAmbiente());
                    row.createCell(1).setCellValue(filho.getDescription() + " - " + item.getUniqueId());
                    row.createCell(2).setCellValue(filho.getComp());
                    row.createCell(3).setCellValue(filho.getLarg());
                    row.createCell(4).setCellValue(filho.getQuant());
                    row.createCell(5).setCellValue("");
                    row.createCell(6).setCellValue(filho.getBord_sup());
                    row.createCell(7).setCellValue(filho.getBord_inf());
                    row.createCell(8).setCellValue(filho.getBord_esq());
                    row.createCell(9).setCellValue(filho.getBord_dir());
                    row.createCell(10).setCellValue("");
                    row.createCell(11).setCellValue(filho.getChapa());
                    row.createCell(12).setCellValue(filho.getEsp());
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

}
