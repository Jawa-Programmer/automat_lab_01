/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpack;

import java.util.Random;

/**
 *
 * @author Spock
 */
public class CaseGenerator {

    private static final Random RAND = new Random();

    /**
     * Генерирует случайную латинскую букву
     *
     * @return случайная буква латинского алфавита
     */
    private static char randomLatinLetter() {
        int let = RAND.nextInt('z' - 'a' + 1);
        boolean cs = RAND.nextBoolean();
        return (char) (let + (cs ? 'a' : 'A'));
    }

    /**
     * Генерирует набор букв заданной длины
     *
     * @param len количество букв
     * @return слово из len букв
     */
    private static String charSequence(int len) {
        String ret = "";
        for (int i = 0; i < len; ++i) {
            ret += randomLatinLetter();
        }
        return ret;
    }

    /**
     * Метод генерирует случайные имена сайтов. Иногда получаются верные, иногда
     * нет
     *
     * @return строка для теста
     */
    public static String genName() {
        String ret = "";
        if (RAND.nextInt(100) < 75) { // с вероятностью 75% мы даем нормальный префикс
            ret = "http://";
        } else {
            ret = charSequence(4) + "://";
        }
        ret += charSequence(RAND.nextInt(50));
        if (RAND.nextInt(100) < 90) { // с вероятностью 90% мы не забудем про точку
            ret += '.';
        }
        ret += charSequence(RAND.nextInt(10));
        if (RAND.nextBoolean()) // с вероятностью 50% мы добавим порт
        {
            ret += ':';
            if (RAND.nextInt(100) < 5) // с вероятностью 5% порт начнется на 0
            {
                ret += '0';
            }
            ret+=RAND.nextInt(100000); // случайный порт, но диапозон случайных чисел в 10 раз больше, чем допустимый диапозон портов
        }
        return ret;
    }
}
