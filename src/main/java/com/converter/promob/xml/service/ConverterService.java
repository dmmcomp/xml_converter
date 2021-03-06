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
    private String clienteName;

    public String executeConversion(InputStream xmlInputStream) throws ParserConfigurationException, IOException, SAXException {
        Document xmlDocument = getDocument(xmlInputStream);
        List<Item> listItems = new ArrayList<>();
        this.clienteName = getClienteName(xmlDocument,"CUSTOMERSDATA");
        process("AMBIENT",xmlDocument, listItems);

        return xlsService.generateXlsFile(listItems);

    }

    public String getClienteName(Document xmlDocument, String bigTag){
        NodeList customerDataNode = xmlDocument.getElementsByTagName(bigTag);

        Element listData = (Element) customerDataNode.item(0);

        NodeList itemList = listData.getElementsByTagName("DATA");
        String clientName = "";
        for(int i = 0; i < itemList.getLength() ; i++){
            Element item = (Element) itemList.item(i);
            String attributeId = item.getAttribute("ID");
            if(attributeId.equals("nomecliente")){
                clientName =  item.getAttribute("VALUE");
                break;
            }
        }
        return clientName;
    }

    private void process(String bigTag,Document xmlDocument, List<Item> listItems) {
        NodeList ambientes = xmlDocument.getElementsByTagName(bigTag);
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
                    itemPai = new Item(
                            this.clienteName,
                            item.getAttribute("OBSERVATIONS"),
                            getIntegerAttribute(item, "UNIQUEID"),
                            true,
                            item.getAttribute("DESCRIPTION"),
                            item.getAttribute("TEXTDIMENSION"),
                            false);

                }else if(isLooseComponent(item)){
                    Item e = itemConstrutor(listaItems, item);
                    e.setClientName(this.clienteName);
                    e.setComponent(true);
                    e.setTextDimension(item.getAttribute("TEXTDIMENSION"));


                    if(doesNotHasItems(item)){
                        listItems.add(e);
                    }else{
                        if (itemPai != null) {
                            listItems.add(itemPai);
                        }
                        itemPai = e;
                    }
                }
                else if (isComponent(item)) {
                    Item ic = itemConstrutor(listaItems, item);
                    itemPai.getChildItems().add(ic);
                }

                if(j == lista.getLength()-1 ){
                    listItems.add(itemPai);
                }
            }
        }
    }

    private Item itemConstrutor(Element listaItems, Element item) {
        Item ic = new Item();
        ic.setAmbiente(listaItems.getAttribute("DESCRIPTION"));
        ic.setUniqueId(getIntegerAttribute(item, "UNIQUEID"));
        ic.setDescription(item.getAttribute("DESCRIPTION"));
        ic.setParentNode(false);
        ic.setQuant(getIntegerAttribute(item, "REPETITION"));
        ic.setComp(item.getAttribute("WIDTH").replaceAll("[.][^.]+$", ""));
        ic.setLarg(item.getAttribute("DEPTH").replaceAll("[.][^.]+$", ""));
        ic.setEsp(item.getAttribute("HEIGHT").replaceAll("[.][^.]+$", ""));
        ic.setObservations(item.getAttribute("OBSERVATIONS"));

        Element  itemReference= (Element) item.getElementsByTagName("REFERENCES").item(0);

        ic.setBord_sup(getBord(itemReference,1));
        ic.setBord_inf(getBord(itemReference,2));
        ic.setBord_esq(getBord(itemReference,3));
        ic.setBord_dir(getBord(itemReference,4));
        String bordaFrontal = getBordaFrontal(itemReference);
        if(bordaFrontal.equals("")){
            ic.setCor_borda(getReferenceAttributeByTag(item,"MODEL_DESCRIPTION_FITA"));
        }else{
            ic.setCor_borda(bordaFrontal);
        }
        ic.setChapa(
                getReferenceAttributeByTag(item,"MATERIAL")
                        +"-"+getReferenceAttributeByTag(item,"THICKNESS")
                        +"-"+getReferenceAttributeByTag(item,"MODEL")
        );
        return ic;
    }

    private boolean isLooseComponent(Element item) {
        return (isComponent(item) && item.getAttribute("UNIQUEPARENTID").equals("-2"));
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

    private String getBordaFrontal(Element itemReference){

        Element bord_sup = (Element) itemReference.getElementsByTagName("MODEL_DESCRIPTION_FITA_FRO").item(0);
        if(bord_sup==null){
            return "";
        }
        String reference = bord_sup.getAttribute("REFERENCE");
        return (reference.isEmpty() ? "" : reference);
    }

    private String getBord(Element itemReference,int borda) {

        Element bord_sup = (Element) itemReference.getElementsByTagName("FITA_BORDA_"+borda).item(0);
        if(bord_sup==null){
            return "";
        }
        return (bord_sup.getAttribute("REFERENCE").equals("1") ? "X" : "");
    }

    private boolean doesNotHasItems(Element item) {
        return item.getElementsByTagName("ITEMS").getLength() == 0;
    }

    private boolean isParentNode(Element produto) {
        return (produto.getAttribute("UNIQUEPARENTID").equals("-2") && !isComponent(produto));
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