package com.code.fictif.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Affectation {
    private String  num_affect;
    private String  num_empl;
    private String  ancien_lieu;
    private String  nouveau_lieu;
    private Date date_affect;
    private Date date_priseservice;


    public String getNum_affect() {
        return num_affect;
    }

    public void setNum_affect(String num_affect) {
        this.num_affect = num_affect;
    }

    public String getNum_empl() {
        return num_empl;
    }

    public void setNum_empl(String num_empl) {
        this.num_empl = num_empl;
    }

    public String getAncien_lieu() {
        return ancien_lieu;
    }

    public void setAncien_lieu(String ancien_lieu) {
        this.ancien_lieu = ancien_lieu;
    }

    public String getNouveau_lieu() {
        return nouveau_lieu;
    }

    public void setNouveau_lieu(String nouveau_lieu) {
        this.nouveau_lieu = nouveau_lieu;
    }

    public Date getDate_affect() {
        return date_affect;
    }

    public void setDate_affect(Date date_affect) {
        this.date_affect = date_affect;
    }

    public Date getDate_priseservice(Date date_priseservice) {
        return date_priseservice;
    }

    public void setDate_priseservice(Date date_priseservice) {
        this.date_priseservice = date_priseservice;
    }
}
