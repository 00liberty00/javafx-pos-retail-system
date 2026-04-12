/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ml.printBarcode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ml.xml.XMLPrinter;

/**
 * Печать ценника
 *
 * @author Dave
 */
public class PrintPriceTag {

        private XMLPrinter xmlPrinter = new XMLPrinter();

    PrintBarcodeService barcodeService = new PrintBarcodeService();

    public PrintPriceTag(String nameGood, String codeGood, int amount, String priceGood) {
        PrinterService printerService = new PrinterService();

        System.out.println(printerService.getPrinters());
        byte[] init = new byte[]{0x1B, 0x40};

        printerService.printBytes("Gprinter GP-2120T", init);

        // cut that paper!
//        byte[] feed = new byte[]{0x1B, 0x33, 5};
//        printerService.printBytes("Gprinter  GP-2120T", feed);
        //print some stuff
//        printerService.printString("Gprinter  GP-2120T", "x");

        /*
    Название.
    Сорт.
    Вес.
    Подпись уполномоченного должностного лица.
    Дата оформления.
         */
        LocalDateTime dateTime = LocalDateTime.now();
		//default format
		//specific format
                String date = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.uu"));
		
        
        
        String cls = barcodeService.CLS();
        String size = "SIZE 43 mm, 25 mm 1\r\n";

        String direction = "DIRECTION 1\r\n";
        String density = "DENSITY 6\r\n";
        String codePage = "CODEPAGE " + 1251 + "\r\n";
        String sound = "SOUND " + 5 + ", " + 200 + "\r\n";
        String reference = "REFERENCE " + 20 + ", " + 10 + "\r\n";
        String ng = barcodeService.TEXT_NAME_GOOD(nameGood);
        String text_some = barcodeService.TEXT_SOME("Цена за 1 шт/кг");
        String text_price = barcodeService.TEXT_PRICE(priceGood + " руб");

//        String feed = barcodeService.Feed(40);
        String text_firm = barcodeService.TEXT_FIRM(xmlPrinter.getHeaderPrinter()+ " " + date);

        String barcode = barcodeService.BARCODE(codeGood);
        String print = barcodeService.PRINT(amount);

        printerService.printString("Gprinter GP-2120T", size + direction + cls + density
                + codePage + ng + text_some + text_price + barcode + text_firm + print + reference);
//        printerService.printString("Gprinter  GP-2120T", cls + barcode + print);
    }

}
