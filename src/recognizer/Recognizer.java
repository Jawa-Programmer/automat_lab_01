package recognizer;

import java.util.HashMap;

/**
 * Класс распознователь декларирует основные методы, которые должны
 * поддерживаться любой реализацией лабы
 *
 * @author JawaProg
 */
public abstract class Recognizer {

    /**
     * Словарь для хранения пар "порт - количество использований"
     */
    protected HashMap<Integer, Integer> stats;

    public Recognizer() {
        stats = new HashMap<Integer, Integer>();
    }

    /**
     * Метод увеличивает количество упоминаний порта в статистике на 1
     *
     * @param port порт для учета в статистике
     */
    protected final void incrementPort(int port) {
        stats.put(port, stats.getOrDefault(port, 0) + 1);
    }

    /**
     * Метод возвращает статистику использованния портов
     *
     * @return строка со статистикой вида "порт - количество"
     */
    public final String getStatistic() {
        StringBuilder bld = new StringBuilder();
        bld.append("порт\t-\tколичество упоминаний\n");
        for (int key : stats.keySet()) {
            bld.append(key).append("\t-\t").append(stats.get(key)).append('\n');
        }
        return bld.toString();
    }

    /**
     * Метод проверяет соотвествие строки шаблону
     * "http://[a-z]{1-20}.[a-z]{1-5}(:[1-9][0-9]{1-4})? В случае соотвествия
     * класс заносит порт в статистику (по умолчанию порт 80)
     *
     * @param to_check строка для проверки
     * @return true, если строка соотвествует шаблону, иначе false
     */
    public abstract boolean checkString(String to_check);

    /**
     * Метод проводит инициализацию автомата
     */
    public abstract void init();
}
