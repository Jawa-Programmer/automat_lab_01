/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smc;

import java.util.HashMap;
import recognizer.Recognizer;
import smc.AutomatFSM.AutomatMap;

/**
 *
 * @author Spock
 */
public class SMCAutomat extends Recognizer {

    private int port;

    private int _counter;
    private boolean acceptable;

    void RESET_COUNTER() {
        _counter = 1;
    }

    void APPEND_DIGIT(char d) {
        port = port * 10 + (d - '0');
    }

    int GET_LETTER_COUNT() {
        return _counter;
    }

    void INC_COUNTER() {
        ++_counter;
    }

    boolean is_latin(char a) {
        return (a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z');
    }
    private AutomatFSM fsm = new AutomatFSM(this);

    public void init() {
        port = 0;
        fsm.restart(); // переводит из любого конечного состояния в стартовое
        // этот дополниетльный переход позволит экономить время, так как не придется пересоздавать объект автомата для каждой новой строки
        acceptable = false;
    }

    void accept() {
        acceptable = true;
    }

    public boolean checkString(String str) {
        init();
        char[] strr = str.toCharArray();
        for (char l : strr) {
            //  System.out.print("letter " + l +": ");
            //  System.out.print(fsm.getState());
            fsm.proc(l);
            //   System.out.print(" -> ");
            //  System.out.println(fsm.getState());
        }
        fsm.end();
        //  System.out.println(fsm.getState());
        if (acceptable) {
            if (port == 0) {
                port = 80;
            }
            incrementPort(port);
        }
        return acceptable;
    }

}
