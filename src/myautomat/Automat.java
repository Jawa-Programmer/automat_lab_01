package myautomat;

import static myautomat.STATE_ABC.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Stack;
import recognizer.Recognizer;

/**
 * Алфавит состояний автомата
 */
enum STATE_ABC {
    INIT, PROHIBITED, A0, A1, A2, A3, A4, A5, A6, B, C0, C1, D0, D1
}

/**
 * Класс детерменированного автомата распознователя с магазинной памятью,
 * определяющего корректность строки http запроса
 */
public final class Automat extends Recognizer{

    /**
     * Множество принимающих состояний автомата
     */
    public static final EnumSet<STATE_ABC> ACCEPTABLE_STATES = EnumSet.of(C1, D1);

    private STATE_ABC state;
    private int amount = 0, port = 0;

    /**
     * проверяет принадлежность буквы латинскому алфавиту (встроенный в java
     * метод выдает true на любом unicode алфавите, что не подходит под задачу)
     *
     * @param a проверяемая буква
     * @return true если это латинская буква нижнего регистра, false в остальных
     * случаях
     */
    private static boolean isLatin(char a) {
        return a >= 'a' && a <= 'z';
    }

    /**
     * Конструктор инициализирует автомат в начальном состоянии
     */
    public Automat() {
        init();
    }

    /**
     * Метод сбрасывает автомат в начальное состояние
     */
    @Override
    public void init() {
        state = INIT;
        amount = 0;
        port = 0;
        stats.clear();
    }

    /**
     * метод принимает очередной сигнал (символ строки) и рассчитывает новое
     * состояние автомата
     *
     * @param ch очередной символ проверяемой строки
     */
    private void pushChar(char ch) {
        ch = Character.toLowerCase(ch); // что бы не различать регистры просто переводим все в маленький
        switch (state) {
            case INIT:
                state = ch == 'h' ? A0 : PROHIBITED;
                break;
            case A0:
                state = ch == 't' ? A1 : PROHIBITED;
                break;
            case A1:
                state = ch == 't' ? A2 : PROHIBITED;
                break;
            case A2:
                state = ch == 'p' ? A3 : PROHIBITED;
                break;
            case A3:
                state = ch == ':' ? A4 : PROHIBITED;
                break;
            case A4:
                state = ch == '/' ? A5 : PROHIBITED;
                break;
            case A5:
                state = ch == '/' ? A6 : PROHIBITED;
                break;
            case A6:
                if (isLatin(ch)) {
                    amount = 1; // помещаем в магазин 1
                    state = B;
                } else {
                    state = PROHIBITED;
                }
                break;
            case B:
                if (isLatin(ch)) {

                    if (amount > 20) {
                        state = PROHIBITED;
                    } else {
                        ++amount;
                    }
                } else if (ch == '.') {
                    state = C0;
                } else {
                    state = PROHIBITED;
                }
                break;
            case C0:
                if (isLatin(ch)) {
                    amount = 1;
                    state = C1;
                } else {
                    state = PROHIBITED;
                }
                break;
            case C1:
                if (isLatin(ch)) {

                    if (amount <= 5) {
                        ++amount;
                    } else {
                        state = PROHIBITED;
                    }
                } else if (ch == ':') {
                    state = D0;
                } else {
                    state = PROHIBITED;
                }
                break;
            case D0:
                if (Character.isDigit(ch) && ch != '0') {
                    amount = 1;
                    state = D1;
                    port = ch - '0';
                } else {
                    state = PROHIBITED;
                }
                break;
            case D1:
                if (Character.isDigit(ch)) {

                    if (amount <= 5) {
                        ++amount;
                        port = port * 10 + (ch - '0');
                    } else {
                        state = PROHIBITED;
                    }
                } else {
                    state = PROHIBITED;
                }
                break;
        }
    }

    /**
     * метод возвращает выходной сигнал автомата
     *
     * @return true, если автомат в принимающем состоянии (строка является
     * корректным http запросом) и false в остальных случаях
     */
    private boolean isAcceptableState() {
        return ACCEPTABLE_STATES.contains(state);
    }

    @Override
    public boolean checkString(String test) {
        init();
        char[] str = test.toCharArray();
        for (char a : str) {
            pushChar(a);
        }
        if (isAcceptableState()) {
            if (port == 0) {
                port = 80;
            }
            incrementPort(port);
            return true;
        }
        return false;
    }
}
