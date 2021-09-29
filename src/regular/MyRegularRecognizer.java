/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import recognizer.Recognizer;

/**
 *
 * @author Spock
 */
public class MyRegularRecognizer extends Recognizer {

    final private Pattern pattern = Pattern.compile("^http://[a-z]{1,20}\\.[a-z]{1,5}(:[1-9]{1}\\d{0,4})?$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean checkString(String to_check) {
        Matcher m = pattern.matcher(to_check);
        boolean ret = m.find();
        if (ret) {
            String port = m.group(1);
            if (port == null) {
                incrementPort(80);
            } else {
                incrementPort(Integer.parseInt(port.substring(1)));
            }
        }
        return ret;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
