/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.mail;

import org.apache.log4j.Logger;
 
public class MailLogger {
 
    private static Logger logger = Logger.getLogger(MailLogger.class);
 
    public static void main(String[] args) {
        try {
            // Generating Sample Exception
            throw new Exception("Generating Exception To Test Log4j Mail Notification...");
        } catch (Exception exObj) {
            logger.error("Sample Result?= " + exObj);
        }
    }
}