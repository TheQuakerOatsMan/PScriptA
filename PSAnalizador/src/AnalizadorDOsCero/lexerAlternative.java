package AnalizadorDOsCero;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextArea;

public class lexerAlternative {
	private Tokens token;
    private String lexema;
    private boolean detener = false;
    private String mensajeError = "";
    private Set<Character> espaciosBlanco = new HashSet<Character>();
    ListaSencilla lineas = new ListaSencilla (); 
    ListaSencilla complete = new ListaSencilla();
    int nlinea = 0;
    int lene = 1;
 
    public void LexerL(String filePath, JTextArea ta) {
    	
    	try {
    		BufferedReader in = new BufferedReader(new FileReader(filePath));
    		String line = null;
    		while ((line = in.readLine()) != null) {
    			line = line.trim();
    			if (!line.equals("")) {
    				lineas.addValue(line);     //anade a lista de lineas 
    				complete.addValue(line);
    			}else {
    				complete.addValue("V");
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
    	while (complete.getValor(lene-1).equals("V")) {
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
        if (lineas.getValor(nlinea).equals("")) {
        	if (lineas.listLenght() == nlinea+1) {
    			detener = true;
    			return;
    		}
			nlinea++;
			lene++;
			while (complete.getValor(lene-1).equals("V")) {
				lene++;
			}
		}
        if (!(lineas.listLenght() == nlinea)) {
        	ignoraEspacios();
            if (findNextToken()) {
                return;
            }
		}        
        detener = true;
        if (lineas.getValor(nlinea).length() > 0) {
        	detener = true;
        	return;
		}
    }

    private void ignoraEspacios() {
    	String templinea=lineas.getValor(nlinea).trim();
    	lineas.EliminarEspec(nlinea);
        lineas.setValueAt(nlinea, templinea);
    }

    private boolean findNextToken() { 
    	//String[] split = lineas.get(nlinea).split(" ");	 
        for (Tokens t : Tokens.values()) {
           // int end = t.endOfMatch(split[0]);
        	int end = t.endOfMatch(lineas.getValor(nlinea));
            if (end != -1) {
            	token = t;
                lexema = lineas.getValor(nlinea).substring(0,end);
                String res=lineas.getValor(nlinea).substring(end,lineas.getValor(nlinea).length());
                lineas.EliminarEspec(nlinea);
                lineas.setValueAt(nlinea, res);
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
