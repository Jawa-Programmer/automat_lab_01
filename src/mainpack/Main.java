/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import jflex.MyLexer;
import myautomat.Automat;
import recognizer.Recognizer;
import regular.MyRegularRecognizer;
import smc.SMCAutomat;

enum Recognizers {
    MY_AUTOMAT, SMC_AUTOMAT, JFLEX_LEXER, REGEX
}

public class Main {

    /// теперь что бы поменять используемую реализацию автомата достаточно поменять одну строчку здесь
    static final Recognizers CURRENT_AUTOMAT = Recognizers.JFLEX_LEXER;

    public static Recognizer getRecognizerInstance() {
        switch (CURRENT_AUTOMAT) {
            case MY_AUTOMAT:
                return new Automat();
            case SMC_AUTOMAT:
                return new SMCAutomat();
            case JFLEX_LEXER:
                return new MyLexer();
            case REGEX:
                return new MyRegularRecognizer();
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Выбирете режим работы:\n0 - ввод с консоли (по умолчанию)\n1 - чтение из файла\n2 - таймирование\n3 - сгенерировать тестовые файлы");
        Recognizer rc = getRecognizerInstance();
        int mode = sc.nextInt();
        sc.nextLine();
        if (mode == 1) {
            System.out.println("Введите имя файла:");
            String filename = sc.nextLine();
            File f = new File(filename);
            if (!f.exists()) {
                System.err.println("Указанный файл не существует!");
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            // что бы таймирование было более честным, я сохраню строки
            // в оперативной памяти, а потом, после завершения таймирования,
            // строки будут записаны в файл
            ArrayList<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            HashMap<String, Boolean> result = new HashMap<String, Boolean>(lines.size());
            // начало таймирования
            long start = System.nanoTime();
            for (String ln : lines) {
                result.put(ln, rc.checkString(ln));
            }
            double total = (System.nanoTime() - start) / 1e6; // из наносекунд сделал милисекунды
            // конец таймирования
            int sz = lines.size();
            lines = null;
            System.gc();

            FileWriter wr = new FileWriter("log.txt");
            wr.write("Имя автомата: " + CURRENT_AUTOMAT + "\n"
                    + "Было проверено " + sz + " строк.\n"
                    + "На про0верку ушло " + total + " мс.\n"
                    + "В среднем на одну строчку уходит " + total / sz + " мс.\n"
                    + "------------------\n"
                    + rc.getStatistic()
                    + "------------------\n");
            for (Entry e : result.entrySet()) {
                wr.append("строка " + e.getKey() + ((Boolean) e.getValue() ? "" : " не") + " удовлетворяет шаблону").append('\n');
            }

            wr.close();
        } else if (mode == 2) {
            File test_catalog = new File("testing");
            if (!test_catalog.exists() || !test_catalog.isDirectory()) {
                System.err.println("Каталог testing не обнаружен");
                return;
            }
            File[] fs = test_catalog.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
            HashMap<Integer, Double> stat = new HashMap<>();
            for (File f : fs) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;

                // что бы таймирование было более честным, я сохраню строки
                // в оперативной памяти, а потом, после завершения таймирования,
                // строки будут записаны в файл
                ArrayList<String> lines = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                // начало таймирования
                long start = System.nanoTime();
                for (String ln : lines) {
                    rc.checkString(ln);
                }
                double total = (System.nanoTime() - start) / 1e6; // из наносекунд сделал милисекунды
                // конец таймирования
                stat.put(lines.size(), total);
                System.gc();
            }
            FileWriter wr = new FileWriter(CURRENT_AUTOMAT + "_timing.txt");
            wr.write("N\tвремя (мс)\n");
            for (Entry e : stat.entrySet()) {
                wr.write(e.getKey() + "\t" + e.getValue().toString().replace('.', ',') + "\n");
            }
            wr.close();
        } else if (mode == 3) {
            File test_catalog = new File("testing");
            if (!test_catalog.exists()) {
                test_catalog.mkdir();
            }
            if (!test_catalog.isDirectory()) {
                System.err.println("Имя testing уже занято!");
                return;
            }
            final int[] cases = {100, 1000, 10000, 100000, 1000000, 10000000, 50000000};
            for (int cs : cases) {
                FileWriter fw = new FileWriter("testing/" + cs + ".txt");
                for (int i = 0; i < cs; ++i) {
                    fw.append(CaseGenerator.genName()).append('\n');
                }
                fw.close();
            }
            System.out.println("Генерация тестовых кейсов завершена");
        } else {
            while (sc.hasNext()) {
                String line = sc.next();
                if (line.equals("END")) {
                    break;
                }
                System.out.println("Строка " + line + (rc.checkString(line) ? "" : " не") + " подходит");
            }
            System.out.println(rc.getStatistic());
        }
    }

}
