/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.print;

import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;

/**
 *
 * @author dave
 */
public class BarcodePrint {

    private final ByteArrayOutputStream printData = new ByteArrayOutputStream();
    private final byte[] charset = {0x1B, 0x74, 6};
    private final byte[] newLine = {0x0A};
    private final byte[] cutLineSpacing = {27, 100, 5};
    private final byte[] cut = {27, 105};
    private final byte leftJustification[] = {0x1B, 0x61, 0};        //выравнивание по левому краю
    private final byte centerJustification[] = {0x1B, 0x61, 49};        //выравнивание по середине
    private final byte rightJustification[] = {0x1B, 0x61, 2};       //выравнивание по правому краю
    private final byte carriageReturn[] = {27, 68, 1, 32, 0};          //Установка позиций горизонтальной табуляции

    public BarcodePrint(String code, int i) {
        byte[] byteCode = code.getBytes();
        String namePrinter = "Gprinter  GP-2120T";
        PrintService tsp100 = getPrintService(namePrinter);
        DocPrintJob job = tsp100.createPrintJob();

        for (int x=0; x<i; x++){
        barcode(byteCode);
        }
        try {
            printData.write(cutLineSpacing);
            printData.write(cut);
        } catch (Exception e) {
            System.out.println("Whoa bro. The printer is balls. Check it:");
            e.printStackTrace();
        }
        
        byte b[] = printData.toByteArray();
        try {
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            job.print(doc, null);
        } catch (PrintException em) {
            em.printStackTrace();
        }

        printData.reset();
    }

    private void barcode(byte[] byteCode) {
        try {
            printData.write(centerJustification);
            byte[] WIDTH_BAR_CODE = {29, 119, 1};
            printData.write(WIDTH_BAR_CODE);
            byte[] HEIGHT_BAR_CODE = {29, 104, 100};
            printData.write(HEIGHT_BAR_CODE);
            byte[] BAR_CODE_1 = {29, 107, 0x04};
            //48, 49, 50, 51, 52, 53, 54, 55, 56, 57
            byte[] BAR_CODE_2 = byteCode;
            byte[] BAR_CODE_3 = {0};
            //byte[] PRINT_BAR_CODE = {0x1D, 0x6B, 0x04, 0x39, 0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x30, 0};
            printData.write(BAR_CODE_1);
            printData.write(BAR_CODE_2);
            printData.write(BAR_CODE_3);
            printData.write(leftJustification);
            byte[] PRINT_BAR_CODE_NUM = {29, 72, 51};
            //byte[] PRINT_BAR_CODE_NUM = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
            printData.write(PRINT_BAR_CODE_NUM);
            //printData.write(newLine);
            //printData.write(cutLineSpacing);
            //printData.write(cut);
        } catch (Exception e) {
            System.out.println("Whoa bro. The printer is balls. Check it:");
            e.printStackTrace();
        }
    }

    private PrintService getPrintService(String serviceName) {
        PrintService[] ps = PrinterJob.lookupPrintServices();
        for (int i = 0; i < ps.length; i++) {
            if (ps[i].getName().indexOf(serviceName) >= 0) {
                return ps[i];
            }
        }
        System.out.println("Aw SNAP! I like, can't find a printer with "
                + serviceName + " in the name dude.");
        System.exit(1);
        return null;
    }
}
