/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.print;

import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * Печать на принтере.
 *
 * @author Dave
 */
public class Print {

    private boolean printOrNot = false;

    /**
     * Печать.
     */
    public void pageSetup(Node node, Stage owner) {

        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null) {
            return;
        }

        //Печать
        print(job, node);

    }

    public boolean getPrint() {
        return printOrNot;
    }

    private void print(PrinterJob job, Node node) {

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.getDefaultPageLayout();

        PageLayout pl = printer.createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), 0, 0, 0, 0);

        //Меняет настройки страницы
        job.getJobSettings().setPageLayout(pl);

        System.out.println("NEW PageLayout: " + pl.toString());

        double scaleX
                = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY
                = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(1, 1);
        node.getTransforms().add(scale);

        System.out.println("Node: " + node.toString());

        // Set the Job Status Message
        //jobStatus.textProperty().bind(job.jobStatusProperty().asString());
        // Print the node
        boolean printed = job.printPage(node);
        node.getTransforms().remove(scale);
        if (printed) {
            job.endJob();
            printOrNot = true;
        }
    }

}
