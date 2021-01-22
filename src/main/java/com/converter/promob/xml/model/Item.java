package com.converter.promob.xml.model;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private boolean isComponent;
    private String clientName;
    private String observations;
    private String ambiente;
    private int uniqueId;
    private boolean parentNode;
    private String description;
    private String textDimension;
    private List<Item> childItems = new ArrayList<>();
    private String comp;
    private String larg;
    private int quant;
    private String material;
    private String bord_sup;
    private String bord_inf;
    private String bord_dir;
    private String bord_esq;
    private String cor_borda;
    private String veio;
    private String chapa;
    private String esp;


    public Item(String clientName,String observations,int uniqueId, boolean parentNode, String description,String textDimension, boolean isComponent) {
        this.clientName = clientName;
        this.observations = observations;
        this.uniqueId = uniqueId;
        this.parentNode = parentNode;
        this.description = description;
        this.isComponent = isComponent;
        this.textDimension = textDimension;
    }
    public Item(){}
    public int getUniqueId() {
        return uniqueId;
    }

    public String getClientName(){return this.clientName;}
    public void setClientName(String clientName){ this.clientName = clientName;}

    public String getObservations(){return this.observations;}

    public void setObservations(String observations){ this.observations = observations;}

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isParentNode() {
        return parentNode;
    }

    public void setParentNode(boolean parentNode) {
        this.parentNode = parentNode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<Item> childItems) {
        this.childItems = childItems;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getLarg() {
        return larg;
    }

    public void setLarg(String larg) {
        this.larg = larg;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }


    public String getVeio() {
        return veio;
    }

    public void setVeio(String veio) {
        this.veio = veio;
    }

    public String getChapa() {
        return chapa;
    }

    public void setChapa(String chapa) {
        this.chapa = chapa;
    }

    public String getEsp() {
        return esp;
    }

    public void setEsp(String esp) {
        this.esp = esp;
    }

    public String getBord_sup() {
        return bord_sup;
    }

    public void setBord_sup(String bord_sup) {
        this.bord_sup = bord_sup;
    }

    public String getBord_inf() {
        return bord_inf;
    }

    public void setBord_inf(String bord_inf) {
        this.bord_inf = bord_inf;
    }

    public String getBord_dir() {
        return bord_dir;
    }

    public void setBord_dir(String bord_dir) {
        this.bord_dir = bord_dir;
    }

    public String getBord_esq() {
        return bord_esq;
    }

    public void setBord_esq(String bord_esq) {
        this.bord_esq = bord_esq;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public boolean isComponent() {
        return isComponent;
    }

    public void setComponent(boolean component) {
        isComponent = component;
    }

    public String getCor_borda() {
        return cor_borda;
    }

    public void setCor_borda(String cor_borda) {
        this.cor_borda = cor_borda;
    }

    public String getTextDimension() {
        return textDimension;
    }

    public void setTextDimension(String textDimension) {
        this.textDimension = textDimension;
    }
}
