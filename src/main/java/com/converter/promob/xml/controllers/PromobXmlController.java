package com.converter.promob.xml.controllers;

import com.converter.promob.xml.service.ConverterService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

//import java.io.File;


@Controller
public class PromobXmlController {

    @Autowired
    private ConverterService converterService;

    @RequestMapping("/")
    @ResponseBody
    public String testeApp()  {
      return "A Aplicação foi inicializada com sucesso!";
    }

    @CrossOrigin
    @PostMapping(value="/execute")
    @ResponseBody
    public String index(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String xlsOutputStream = converterService.executeConversion(file.getInputStream());
        return xlsOutputStream;
    }
}
