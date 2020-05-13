package AnalizadorDOsCero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Tokens {
	si("si"),
	para("para"),
	sino("sino"),
	contra("contra"),
	mientras("mientras"),
	hacer("hacer"),
	ala("ala"),
	mod("mod"),
	tallo("tallo"),
	ruptura("ruptura"),
	cambio("cambio"),
	caso("caso"),
	principal("principal"),
	ejecuta("ejecuta"),
	defaulti("default"),
	ent("ent"),
	dec("dec"),
	cad("cad"),
	cart("cart"),
	bool("bool"),
	inicio("inicio"),
	finale("final"),
	verdadero("verdadero"),
	falso("falso"),
	log_not("!!"),
	log_and("&"),
	log_or("[|][|]"),
	crear("crear"),
	funcion("funcion"),
	clase("clase"),
	id_clase(":[_]?[A-Z|a-z|0-9]+"),
	id_func("::[_]?[A-Z|a-z|0-9]+"),
	imprime("imprime"),
	lectura("lectura"),
	retorna("retorna"),
	identificador("@[_]?[A-Z|a-z|0-9]+"),
	identificadorC("%%[_]?[A-Z|a-z|0-9]+"),
	identificadorF("%[_]?[A-Z|a-z|0-9]+"),
	id_dec("[-]?([1-9][0-9]+[.][0-9][1-9]+|0[.][0-9][1-9]+|[1-9][0-9]+[.]0)([eE][+-][1-9][0-9]+[1-9])?"),
    id_ent("[-]?(0|([1-9][0-9]*))"),
    id_cad("[\"][[\\w-]|[@]|[=/*-+]|[:.,{}';]|[\"]|[\\s]]+[\"]"),
    id_cart("[\'][[\\w-]|[@]|[=/*-+]|[:.,{}';]|[\"]|[\\s]][\']"),
    op_asig("[-][>]"),
    op_cond("[?][:]"),
    op_negacion("[!]"),
    op_div("[/]"),
    op_mult("[*]"),
    op_sum("[+]"),
    op_res("[-]"),
    op_comp("[==||!=]"),
    op_may("[>]"),
    op_min("[<]"),
    op_mayk("[>][=]"),
    op_mink("[<][=]"),
    puntcoma("[;]"),
    abP("[(]"),
    ciP("[)]"),
    ini_com("[*][/]"),
    fin_com("[/][*]"),
    del_id(","),
    error("[[\\w-]|[@#^-_?~`\\|]|[:.{}']|[\"]|[\\s]]"),
    comment("[#]");
	
    private final Pattern pattern;
    Tokens(String regex) {
        pattern = Pattern.compile("^" + regex);
    }
   
    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);
        if (m.find()) {
            return m.end();
        }
        return -1;
    }
}