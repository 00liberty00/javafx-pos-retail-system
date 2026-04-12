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
public class PrintBarcode {

    PrintBarcodeService barcodeService = new PrintBarcodeService();

    public PrintBarcode(String text, String barcode, int amount) {
        PrinterService printerService = new PrinterService();

        System.out.println(printerService.getPrinters());
        byte[] init = new byte[]{0x1B, 0x40};

        printerService.printBytes("Gprinter GP-2120T", init);

        // cut that paper!
//        byte[] feed = new byte[]{0x1B, 0x33, 5};
//        printerService.printBytes("Gprinter  GP-2120T", feed);
        //print some stuff
//        printerService.printString("Gprinter  GP-2120T", "x");
        String cls = barcodeService.CLS();
        String direction = "DIRECTION 1\r\n";
        String density = "DENSITY 6\r\n";
        String codePage = "CODEPAGE " + 1251 + "\r\n";
        String sound = "SOUND " + 5 + ", " + 200 + "\r\n";
        String reference = "REFERENCE " + 20 + ", " + 10 + "\r\n";
        String t = barcodeService.TEXT_NAME_GOOD(text);
        String feed = barcodeService.Feed(40);
        String b = barcodeService.BARCODE(barcode);
        String print = barcodeService.PRINT(amount);

        printerService.printString("Gprinter GP-2120T", direction + cls + density + codePage + t + b + print + reference);
//        printerService.printString("Gprinter  GP-2120T", cls + barcode + print);
    }

}
