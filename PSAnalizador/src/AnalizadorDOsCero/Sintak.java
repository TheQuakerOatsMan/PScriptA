package AnalizadorDOsCero;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

public class Sintak {
	
	ArrayList<String> lex = new ArrayList<String>();          	// Lo que arroja el analisis lexico
	ArrayList<String> elexe = new ArrayList<String>();			// Fila de entrantes
	ArrayList<String> terminales = new ArrayList<String>();		// Columna de terminales
	Stack<String> pila = new Stack<String>();
	String MensajeDeError = "";
	String MensajeDePila = "";
	String [][] tabla1;
	int linea = 0 ;
	boolean errP = false; 
	
	//Este metodo llena la fila y columna en los arrays creados para ahorrarnos bucles de búsqueda
	public void llenarFyC() {
		for (int i = 0; i < tabla1.length; i++) {
			terminales.add(tabla1[i][0]);
		}
		for (int i = 0; i < tabla1[0].length; i++) {
			elexe.add(tabla1[0][i]);
		}
	}
	
	//Este es el constructor que recibe todo el pedo y inicia lo esensial
	public Sintak() {
		tablas t = new tablas();
		tabla1 = t.laperrona;
		llenarFyC();

		pila.push("prog");
	}
	
	
	//Revisa si es aceptado
	public boolean aceptado() {
		if (pila.isEmpty() && MensajeDeError.isEmpty()) {
			pila.clear();
			return true;
		}else {
			//MensajeDeError += "Error de Sintaxis3: "+lex.get(lex.size()-1)+" después de "+ lex.get(lex.size()-1)+" en la línea "+ linea+"\n";errP = false;
			return false;
		}
	}
	
	//Este es el único metodo que se llama
	public boolean AS(String lexema, int line) {
		linea = line;
		MensajeDePila = "";
		lex.add(lexema);
		procesoApilAndDesapil(lex.size()-1);
		return errP;
	}
	
	//Este nos va a servir para llamarlo y mediante recursivida poder llenar hasta que se desapile y concuerde y retorne a AS
	public void procesoApilAndDesapil (int pivote) {
		if(pila.isEmpty()) {
			MensajeDeError += "Error de Sintaxis1: "+lex.get(pivote)+" después de "+ lex.get(pivote-1)+" en la línea "+ linea+"\n";errP = false;
			pila.push(" ");
		}else if (terminales.contains(pila.peek()) && elexe.contains(lex.get(pivote))) {
			apila(terminales.indexOf(pila.peek()), elexe.indexOf(lex.get(pivote)),pivote);
		}else if(elexe.contains(lex.get(pivote)) && pila.contains(lex.get(pivote))) {//en este caso determina si el teminal se ecuentra el la produccion y no pare
			proceso(pivote);
		}
	}
	public void proceso(int pos) {
		if (pila.peek().equalsIgnoreCase(lex.get(pos))) {
			pila.pop();
			MensajeDePila += pila+"\n";errP = true;
		} else {
			MensajeDePila += pila+"\n";
			procesoApilAndDesapil(pos);
		}
	}
	
	//Aqui apila hasta lo indicado en procesoApilAndDesapil()
	public void apila(int i, int j, int pivote) {
		String interseccion = tabla1[i][j];
		System.out.println("interseccion "+tabla1[i][j]);
		if (interseccion == " ") {
			if (pivote > 0) {
				MensajeDeError += "Error de Sintaxis2: "+lex.get(pivote)+" después de "+ lex.get(pivote-1)+" en la línea "+ linea+"\n" ; errP = false;
			}else {
				MensajeDeError += "Error de Sintaxis3: "+lex.get(pivote)+" al inicio de la línea 1\n" ; errP = false;
			}
		}else {
			String[] interseccionArray = interseccion.split(" ");
			pila.pop();
			for (int k = interseccionArray.length; k > 0; k--) {
				pila.push(interseccionArray[k - 1]);
			}
			if (pila.peek().equalsIgnoreCase("ç")) {
				pila.pop();
			}
			if (pila.peek().equalsIgnoreCase(lex.get(pivote))) {
				MensajeDePila += pila+"\n";
				pila.pop();
				MensajeDePila += pila+"\n";errP = true;
			} else {
				MensajeDePila += pila+"\n";
				procesoApilAndDesapil(pivote);
			}
		}
	}
}