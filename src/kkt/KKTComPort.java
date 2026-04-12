/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.kkt;

/**
 *
 * @author Dave
 */
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class KKTComPort {

    String[] portNames;
    //Список занятых com-портов
    public KKTComPort() {
        portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }
    //Возвращает список занятых com-портов
    public String[] comPort() {
        return portNames;
    }
}
