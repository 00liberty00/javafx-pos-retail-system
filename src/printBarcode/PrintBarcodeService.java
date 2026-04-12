/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.printBarcode;

/**
 *
 * @author Dave
 */
public class PrintBarcodeService {

    public String CLS() {
        return "CLS\r\n";
    }

    public String TEXT_NAME_GOOD(String text) {
        return "TEXT " + 2 + "," + 0 + "," + "\"" + 10 + "\"" + "," + 0 + ","
                + 1 + "," + 1 + "," + "\"" + text + "\"" + "\r\n";
    }

    public String TEXT_SOME(String text) {
        return "TEXT " + 2 + "," + 25 + "," + "\"" + 10 + "\"" + "," + 0 + ","
                + 1 + "," + 1 + "," + "\"" + text + "\"";
    }

    public String TEXT_PRICE(String text) {
        return "TEXT " + 190 + "," + 50 + "," + "\"" + 10 + "\"" + "," + 0 + ","
                + 1 + "," + 2 + "," + "\"" + text + "\"" + "\r\n";
    }

    public String TEXT_FIRM(String text) {
        return "TEXT " + 2 + "," + 145 + "," + "\"" + 10 + "\"" + "," + 0 + ","
                + 1 + "," + 1 + "," + "\"" + text + "\"" + "\r\n";
    }

    public String BARCODE(String barcode) {
        return "BARCODE " + 5 + "," + 105 + "," + "\"" + "128" + "\"" + "," + 10 + ","
                + 1 + "," + 0 + "," + 2 + "," + 4 + "," + "\"" + barcode + "\"" + "\r\n";

    }

    public String PRINT(int amount) {
        return "PRINT " + amount + "\r\n";
    }

    public String Feed(int dot) {
        return "FEED " + dot + "\r\n";
    }
}
