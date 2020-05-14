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
	
    private Tokens token;
    private String lexema;
    private boolean detener = false;
    private String mensajeError = "";
    private Set<Character> espaciosBlanco = new HashSet<Character>();
    ListaSencilla lineas = new ListaSencilla (); 
    ListaSencilla complete = new ListaSencilla();
    int nlinea = 0;
    int lene = 1;
    char listaArg[];
 
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
    		mensajeError += "El archivo est� en blanco" + filePath;
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
        ignoraEspacios();
        listaArg=lineas.getValor(nlinea).toCharArray();
        for (int i = 0; i < listaArg.length; i++) {
			//pa que se vea el contenido
        	System.out.print(""+listaArg[i]);
		}
        System.out.println("");
        if (!proceso()==true) {
        	return;
        }
        /*if (!(lineas.listLenght() == nlinea)) {
        	ignoraEspacios();
            if (findNextToken()) {
                return;
            }
		}        
        detener = true;
        if (lineas.getValor(nlinea).length() > 0) {
        	detener = true;
        	return;
		}*/
    }
    //variables de puntero
    int inicio=0,fin=0,caso=0;
	String arg="";
    public boolean proceso() {
    	for ( fin=0; fin < listaArg.length;) {
    		if (listaArg[fin]=='\n' || listaArg[fin]==' ') {
    			fin=fin+1;
	  		  	inicio=fin;
	  		  	arg="";
	  		  	proceso();
    		}
    		arg+=""+listaArg[fin];
    		if (fin+1==listaArg.length) {
    			if (!arg.equalsIgnoreCase(" ")) {
    				if (verifica(arg)) {
   	     			 fin=fin+1;
   	  		  		inicio=fin;
   	  		  		arg=""; //se limpia el jale
   	  		  		return false;
   	     		  }	
    			}
    			return false;
    		}else {
    			fin++;
    	     	   if (listaArg[fin]=='\n' || listaArg[fin]==' ') {
    	     		  if (verifica(arg)) {
    	     			 fin=fin+1;
    	  		  		inicio=fin;
    	  		  		arg=""; //se limpia el jale
    	  		  	return false;
    	     		  }	
    	     	   }
    		}
		}
    	return false;
    }

    public boolean verifica(String arg) {
    	for (Tokens t : Tokens.values()) {
            // int end = t.endOfMatch(split[0]);
         	boolean s=t.match(arg);
             if (s != false) {
             	token = t;
             	System.out.println(token.toString());
             		lexema=arg;
             		int end = t.endOfMatch(arg);
                    if (end != -1) {
                    	String templinea=lineas.getValor(nlinea).trim();
                    	lineas.EliminarEspec(nlinea);
                        lineas.setValueAt(nlinea, templinea);
                        String res=lineas.getValor(nlinea).substring(end,lineas.getValor(nlinea).length());
                        System.out.println("res: "+res);
                        lineas.EliminarEspec(nlinea);
                        lineas.setValueAt(nlinea, res);
                    }
                 return true;
             }
             }
         return false;
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