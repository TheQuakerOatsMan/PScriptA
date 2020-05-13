package AnalizadorDOsCero;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Lexer {
	
    private StringBuilder entrada = new StringBuilder();
    private Tokens token;
    private String lexema;
    private boolean detener = false;
    private String mensajeError = "";
    private Set<Character> espaciosBlanco = new HashSet<Character>();
    ArrayList<String> lineas = new ArrayList<String>();
    ArrayList<String> complete = new ArrayList<String>();
    int nlinea = 0;
    int lene = 1;
 
    public void LexerL(String filePath, JTextArea ta) {
    	
    	try {
    		BufferedReader in = new BufferedReader(new FileReader(filePath));
    		String line = null;
    		while ((line = in.readLine()) != null) {
    			line = line.trim();
    			if (!line.equals("")) {
    				lineas.add(line);     //anade a lista de lineas 
    				complete.add(line);
    			}else {
    				complete.add("V");
    			}
    		}
    	}catch (IOException e) {
    		detener = true;
    		mensajeError += "Error en lectura de archivo: " + filePath;
    		return;
		}
    	if (lineas.isEmpty()) {
    		detener = true;
    		mensajeError += "El archivo está en blanco" + filePath;
    		return;
    	}
    	while (complete.get(lene-1).equals("V")) {
			lene++;
			//esto para si empieza con puros espacios no omi
		}
        espaciosBlanco.add('\r');
        espaciosBlanco.add('\n');
        espaciosBlanco.add('\t');
        espaciosBlanco.add((char) 8);
        espaciosBlanco.add((char) 9);
        espaciosBlanco.add((char) 10);
        espaciosBlanco.add((char) 11);
        espaciosBlanco.add((char) 12);
        espaciosBlanco.add((char) 13);
        espaciosBlanco.add((char) 32); 	
        siguiente();
    }

    public void siguiente() {
        if (detener) {
            return;
        }
        if (lineas.get(nlinea).equals("")) {
        	if (lineas.size() == nlinea+1) {
    			detener = true;
    			return;
    		}
			nlinea++;
			lene++;
			while (complete.get(lene-1).equals("V")) {
				lene++;
			}
		}
        if (!(lineas.size() == nlinea)) {
        	ignoraEspacios();
            if (findNextToken()) {
                return;
            }
		}        
        detener = true;
        if (lineas.get(nlinea).length() > 0) {
        	detener = true;
        	return;
		}
    }

    private void ignoraEspacios() {
        lineas.set(nlinea, lineas.get(nlinea).trim());
    }

    private boolean findNextToken() { 
    	//String[] split = lineas.get(nlinea).split(" ");	 
        for (Tokens t : Tokens.values()) {
           // int end = t.endOfMatch(split[0]);
        	int end = t.endOfMatch(lineas.get(nlinea));
            if (end != -1) {
                token = t;
                lexema = lineas.get(nlinea).substring(0,end);
                lineas.set(nlinea, lineas.get(nlinea).substring(end,lineas.get(nlinea).length()));
                return true;
            }
        }
        return false;
    }

    public Tokens currentToken() {
        return token;
    }
    public String currentLexema() {
        return lexema;
    }

    public boolean isSuccessful() {
        return mensajeError.isEmpty();
    }

    public String mensajeError() {
        return mensajeError;
    }

    public boolean isExausthed() {
        return detener;
    }
}