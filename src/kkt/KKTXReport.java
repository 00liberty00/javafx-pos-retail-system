/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.kkt;

import ru.atol.drivers10.fptr.IFptr;

/**
 *
 * @author Dave
 */
public class KKTXReport {

    public KKTXReport() {
    }

    public void xReport(IFptr fptr) {
        fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_X);
        fptr.report();
    }

}
