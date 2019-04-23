package com.converter.promob.xml.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.converter.promob.xml")
@SpringBootApplication
//@Controller
public class Configuracao {


    public static void main(String[] args) {
        SpringApplication.run(Configuracao.class,args);
    }
}
