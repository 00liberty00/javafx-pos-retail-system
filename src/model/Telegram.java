/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ml.model;

/**
 *
 * @author Dave
 */
public class Telegram implements java.io.Serializable {

    private Integer idTelegram;
    private String tokenBot;
    private String nameBot;
    private String idChat;
    private Boolean progclientBot;

    public Integer getIdTelegram() {
        return idTelegram;
    }

    public void setIdTelegram(Integer idTelegram) {
        this.idTelegram = idTelegram;
    }

    public String getTokenBot() {
        return tokenBot;
    }

    public void setTokenBot(String tokenBot) {
        this.tokenBot = tokenBot;
    }

    public String getNameBot() {
        return nameBot;
    }

    public void setNameBot(String nameBot) {
        this.nameBot = nameBot;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public Boolean getProgclientBot() {
        return progclientBot;
    }

    public void setProgclientBot(Boolean progclientBot) {
        this.progclientBot = progclientBot;
    }

}
