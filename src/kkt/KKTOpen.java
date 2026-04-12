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
import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;

public class KKTOpen {

    public KKTOpen() {
        //Инициализация драйвера
        IFptr fptr = new Fptr();

        //Настройка драйвера
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_LIBRARY_PATH, String.valueOf("java.library.path"));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_AUTO));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_COM));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_COM_FILE, "9");
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_BAUDRATE, String.valueOf(IFptr.LIBFPTR_PORT_BR_115200));
        fptr.applySingleSettings();
        
        String settings = fptr.getSettings();
        System.out.println("ОТВЕТ : "+ settings.substring(6, 100));
        fptr.open();

        boolean isOpened = fptr.isOpened();

        //Запрос информации о ККТ
        fptr.setParam(IFptr.LIBFPTR_PARAM_DATA_TYPE, IFptr.LIBFPTR_DT_STATUS);
        fptr.queryData();
        String modelName = fptr.getParamString(IFptr.LIBFPTR_PARAM_MODEL_NAME);
        
        System.out.println("open : " + modelName);

        //X-Отчет
        //fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_X);
        //fptr.report();
        //открытие нефискального чека
        fptr.beginNonfiscalDocument();

        //печать картинки с диска на компе
        fptr.setParam(IFptr.LIBFPTR_PARAM_FILENAME, "src/ml/resources/image/logo.png");
        fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_CENTER);
        fptr.printPicture();
        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "");
        fptr.printText();
        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, "");
        fptr.printText();
        //закрытие нефискального чека
        fptr.endNonfiscalDocument();

        //fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
        fptr.close();

        //Деинициализация драйвера
        fptr.destroy();
    }

}
