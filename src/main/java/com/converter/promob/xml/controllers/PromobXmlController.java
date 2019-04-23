package com.converter.promob.xml.controllers;

import com.converter.promob.xml.model.Item;
import com.converter.promob.xml.service.ConverterService;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import java.io.File;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Controller
public class PromobXmlController {

    @Autowired
    private ConverterService converterService;

    @RequestMapping("/")
    @ResponseBody
    public String testeApp()  {
      return "A Aplicação foi inicializada com sucesso!";
    }

    @PostMapping(value="/execute")
    @ResponseBody
    public void index(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ByteArrayOutputStream xlsOutputStream = converterService.executeConversion(file.getInputStream());
        response.setHeader("Content-Disposition", "attachment; filename="
                + "teste.xls");
        response.getOutputStream().write(xlsOutputStream.toByteArray());
    }
}
