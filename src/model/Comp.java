/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.model;

import java.util.Date;

/**
 *
 * @author Dave
 */
public class Comp {

    private Integer idComp;
    private byte[] mac;
    private Integer ip;
    private String name;
    private Date dateCreate;
    private String note;
    private boolean blocking;
    private Integer idLicense;

    public Comp() {
    }

    public Comp(Integer idComp, byte[] mac, Integer ip, String name, Date dateCreate, String note, boolean blocking, Integer idLicense) {
        this.idComp = idComp;
        this.mac = mac;
        this.ip = ip;
        this.name = name;
        this.dateCreate = dateCreate;
        this.note = note;
        this.blocking = blocking;
        this.idLicense = idLicense;
    }

    public Integer getIdComp() {
        return idComp;
    }

    public void setIdComp(Integer idComp) {
        this.idComp = idComp;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public Integer getIp() {
        return ip;
    }

    public void setIp(Integer ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public Integer getIdLicense() {
        return idLicense;
    }

    public void setIdLicense(Integer idLicense) {
        this.idLicense = idLicense;
    }

}
