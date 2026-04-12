/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.print;

import java.util.ArrayList;
import java.util.List;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 *
 * @author dave
 */
public class PrinterName {

    private PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    private List nameList = new ArrayList();

    public void printerName() {
        String name;
        for (PrintService printService : printServices) {
            name = printService.getName();
            nameList.add((String) name);
        }
    }

    public List displayResult() {
        return nameList;
    }

}
