package jflex;

enum LEXEM_TYPES {PREFIX, NAME, DOUBLE_POINT, PORT, POINT}

class MyLexem
{
	private LEXEM_TYPES type;
	private String val;
	private int len;
	public MyLexem(LEXEM_TYPES t, String v, int l)
	{
	type = t;
	val = v;
	len = l;
	}
	public String getValue(){return val;}
	public LEXEM_TYPES getType(){return type;}
	public int getLength(){return len;}
	public String toString(){return type + " - " + val;}
}
%%

PREFIX = "http://"
NAME = [a-zA-Z]{1,20}
PORT = [1-9][0-9]{0,4}

%%

{PREFIX} {return new MyLexem(LEXEM_TYPES.PREFIX, yytext(), yylength());}
{NAME} {return new MyLexem(LEXEM_TYPES.NAME, yytext(), yylength());}
{PORT} {return new MyLexem(LEXEM_TYPES.PORT, yytext(), yylength());}
":" {return new MyLexem(LEXEM_TYPES.DOUBLE_POINT, yytext(), yylength());}
"." {return new MyLexem(LEXEM_TYPES.POINT, yytext(), yylength());}