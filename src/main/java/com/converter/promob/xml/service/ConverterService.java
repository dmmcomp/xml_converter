package com.converter.promob.xml.service;

import com.converter.promob.xml.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConverterService {

    @Autowired
    private  XlsService xlsService;

    public ByteArrayOutputStream executeConversion(InputStream xmlInputStream) throws ParserConfigurationException, IOException, SAXException {
        Document xmlDocument = getDocument(xmlInputStream);
        List<Item> listItems = new ArrayList<>();

        NodeList ambientes = xmlDocument.getElementsByTagName("AMBIENT");
        for (int i = 0; i < ambientes.getLength(); i++) {
            Element listaItems = (Element) ambientes.item(i);

            NodeList lista = listaItems.getElementsByTagName("ITEM");
            Item itemPai = null;

            for (int j = 0; j < lista.getLength(); j++) {

                Element item = (Element) lista.item(j);

                if (isParentNode(item)) {
                    if (itemPai != null) {
                        listItems.add(itemPai);
                    }
                    itemPai = new Item(getIntegerAttribute(item, "UNIQUEID"), true, item.getAttribute("DESCRIPTION"));
                } else if (doesNotHasItems(item) && isComponent(item)) {
                    Item ic = new Item();
                    ic.setAmbiente(listaItems.getAttribute("DESCRIPTION"));
                    ic.setUniqueId(getIntegerAttribute(item, "UNIQUEID"));
                    ic.setDescription(item.getAttribute("DESCRIPTION"));
                    ic.setParentNode(false);
                    ic.setQuant(getIntegerAttribute(item, "REPETITION"));
                    ic.setComp(item.getAttribute("WIDTH"));
                    ic.setLarg(item.getAttribute("DEPTH"));
                    ic.setEsp(item.getAttribute("HEIGHT"));

                    Element  itemReference= (Element) item.getElementsByTagName("REFERENCES").item(0);

                    ic.setBord_sup(getBord(itemReference,1));
                    ic.setBord_inf(getBord(itemReference,2));
                    ic.setBord_esq(getBord(itemReference,3));
                    ic.setBord_dir(getBord(itemReference,4));
                    ic.setChapa(
                            getReferenceAttributeByTag(item,"MATERIAL")
                                    +"-"+getReferenceAttributeByTag(item,"THICKNESS")
                                    +"-"+getReferenceAttributeByTag(item,"MODEL")
                    );
                    itemPai.getChildItems().add(ic);
                }
            }
        }


        return xlsService.generateXlsFile(listItems);

    }

    public boolean isComponent(Element item){
        return (item.getAttribute("COMPONENT").equals("Y"));
    }

    private int getIntegerAttribute(Element item, String attribute) {
        if(item == null){
            System.out.println("teste");
        }
       return (item.getAttribute(attribute) != null ? Integer.parseInt(item.getAttribute(attribute)) : 0);
    }

    public String getReferenceAttributeByTag(Element itemReference, String tag){

        Element itemTag = (Element) itemReference.getElementsByTagName(tag).item(0);
        if(itemTag==null){
            return "";
        }
        return itemTag.getAttribute("REFERENCE");
    }

    private int getBord(Element itemReference,int borda) {

        Element bord_sup = (Element) itemReference.getElementsByTagName("FITA_BORDA_"+borda).item(0);
        if(bord_sup==null){
            return 0;
        }
        return getIntegerAttribute(bord_sup, "REFERENCE");
    }

    private boolean doesNotHasItems(Element item) {
        return item.getElementsByTagName("ITEMS").getLength() == 0;
    }

    private boolean isParentNode(Element produto) {
        return produto.getAttribute("UNIQUEPARENTID").equals("-2");
    }

    private Document getDocument(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlInputStream);
    }
}


//        XPath xPath = XPathFactory.newInstance().newXPath();
//
//        String expression = "/LISTING/AMBIENTS/AMBIENT/CATEGORIES/CATEGORY";
//
//        XPathExpression xPathExpression = xPath.compile(expression);
//        NodeList lista = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

//            System.out.println("--------------INICIO-ITEM--------------");
//            System.out.println("DESCRIÇÃO: "+produto.getAttribute("DESCRIPTION"));
//            System.out.println("REFERENCIA: "+produto.getAttribute("REFERENCE"));
//            System.out.println("UNIQUEID: "+produto.getAttribute("UNIQUEID"));
//            System.out.println("UNIQUEPARENTID: "+produto.getAttribute("UNIQUEPARENTID"));
//            System.out.println("--------------FIM-ITEM--------------\n");