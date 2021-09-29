/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jflex;

import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jflex.LEXEM_TYPES.PREFIX;
import recognizer.Recognizer;

/**
 *
 * @author Spock
 */
public class MyLexer extends Recognizer {

    @Override
    public void init() {
        stats.clear();
    }

    private static Yylex lexer = new Yylex(null);

    @Override
    public boolean checkString(String s) {
        lexer.yyreset(new StringReader(s));
        try {
            MyLexem lx = lexer.yylex();
            if (lx == null || lx.getType() != PREFIX) {
                return false;
            }
            lx = lexer.yylex();
            if (lx == null || lx.getType() != LEXEM_TYPES.NAME || lx.getLength() > 20) {
                return false;
            }

            lx = lexer.yylex();
            if (lx == null || lx.getType() != LEXEM_TYPES.POINT) {
                return false;
            }

            lx = lexer.yylex();
            if (lx == null || lx.getType() != LEXEM_TYPES.NAME || lx.getLength() > 5) {
                return false;
            }
            lx = lexer.yylex();
            if (lx == null) {
                stats.put(80, stats.getOrDefault(80, 0) + 1);
                return true;
            } else if (lx.getType() != LEXEM_TYPES.DOUBLE_POINT) {
                return false;
            }
            lx = lexer.yylex();
            if (lx == null || lx.getType() != LEXEM_TYPES.PORT || lx.getLength() > 5) {
                return false;
            }
            int port = Integer.parseInt(lx.getValue());
            stats.put(port, stats.getOrDefault(port, 0) + 1);
            lx = lexer.yylex();
            if (lx == null) {
                return true;
            }
        } catch (IOException | Error er) {
        } finally {
            try {
                lexer.yyclose();
            } catch (IOException ex) {
                Logger.getLogger(MyLexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

}
