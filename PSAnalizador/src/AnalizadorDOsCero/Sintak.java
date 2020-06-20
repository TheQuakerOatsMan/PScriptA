package AnalizadorDOsCero;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

public class Sintak {
	
	ListaSencilla lex= new ListaSencilla();         	// Lo que arroja el analisis lexico
	ListaSencilla elexe= new ListaSencilla();			// Fila de entrantes
	ListaSencilla terminales= new ListaSencilla();		// Columna de terminales
	Stack<String> pila = new Stack<String>();
	String MensajeDeError = "";
	String MensajeDePila = "";
	String [][] tabla1;
	int linea = 0, cerra=0, cera=0;
	boolean errP = false; 
	boolean vuelta = false; //controla el recorrido 
	
	//Este metodo llena la fila y columna en los arrays creados para ahorrarnos bucles de búsqueda
	public void llenarFyC() {
		for (int i = 0; i < tabla1.length; i++) {
			terminales.addValue(tabla1[i][0]);
		}
		for (int i = 0; i < tabla1[0].length; i++) {
			elexe.addValue(tabla1[0][i]);
		}
	}
	
	//Este es el constructor que recibe todo el pedo y inicia lo esensial
	public Sintak() {
		vuelta=false;
		tablas t = new tablas();
		//tabla1 = t.laperrona;
		tabla1 = t.laperrona2;
		llenarFyC();
		cera=0;
		pila.push("clas");
	}
	
	
	//Revisa si es aceptado
	public boolean aceptado() {
		if (pila.isEmpty() && MensajeDeError.isEmpty()) {
			pila.clear();
			return true;
		}else {
			//MensajeDeError += "Error de Sintaxis3: "+lex.getValor(lex.listLenght()-1)+" después de "+ lex.getValor(lex.listLenght()-1)+" en la línea "+ linea+"\n";errP = false;
			return false;
		}
	}
	public int verfinales() {
		return cera;
	}
	//Este es el único metodo que se llama
	public boolean AS(String lexema, int line) {
		linea = line;
		/*MensajeDePila = "";
		lex.addValue(lexema);
		procesoApilAndDesapil(lex.listLenght()-1);
		return errP;*/
		if (vuelta) {
			MensajeDePila = "";
		}else {
			MensajeDePila += pila;
			vuelta = true;
		}
		lex.addValue(lexema);
		if(lexema.equals("abP") && cerra<1) {
			cerra ++;
		}
		procesoApilAndDesapil(lex.listLenght()-1);
		if(lexema.equals("ciP")) { //debe poder sacar una llave
			if (cerra > 0) {
				cerra --;
				System.out.println("contador1 "+cerra);
			}
		}
		
		return errP;
	}
	public boolean finales(String lexema, int line) {
		linea=line;
			//la hace falta varios finales
		System.out.println("en este momento hay ceras-----:"+cera);
				lex.addValue("finale");
				apila(terminales.indexOf(pila.peek()), elexe.indexOf(lex.getValor(lex.listLenght()-1)),lex.listLenght()-1);
				MensajeDeError += "Error de Sintaxis3: en la linea "+linea+"\n"
						+ "Se esperaba otro "+lex.getValor(lex.listLenght()-1)+" para poder terminar correctamente\n";
				cera--;
				errP=false;
		return errP;
	}
	
	//Este nos va a servir para llamarlo y mediante recursivida poder llenar hasta que se desapile y concuerde y retorne a AS
	public void procesoApilAndDesapil (int pivote) {
		if(pila.isEmpty()) {
			MensajeDeError += "Error de Sintaxis1: "+lex.getValor(pivote)+" después de "+ lex.getValor(pivote-1)+" en la línea "+ linea+"\n"
					+ "La pila esta vacia\n";errP = false;
			//pila.push(" ");
		}else if (terminales.contiene(pila.peek()) && elexe.contiene(lex.getValor(pivote))) {
			apila(terminales.indexOf(pila.peek()), elexe.indexOf(lex.getValor(pivote)),pivote);
		}else if(elexe.contiene(lex.getValor(pivote)) && pila.contains(lex.getValor(pivote))) {//en este caso determina si el teminal se ecuentra el la produccion y no pare
			proceso(pivote);
		}
	}
	public void iniciofinale (String cad ) {
		if (cad.equalsIgnoreCase("inicio"))
			cera++;
		else if (cad.equalsIgnoreCase("finale") || cad.equalsIgnoreCase("endproc") || cad.equalsIgnoreCase("endf") || cad.equalsIgnoreCase("endw") || cad.equalsIgnoreCase("endif"))
			cera--;
	}
	public void proceso(int pos) { //en este caso se ira en el caso de que este en un proceso corrido
		if (pila.peek().equalsIgnoreCase(lex.getValor(pos))) {
			pila.pop();
			MensajeDePila += pila+"\n";errP = true;
			//iniciofinale(lex.getValor(pos));
		} else {
			if (terminales.indexOf(pila.peek())==-1) {//checa si no existe la palabra no es un no terminal
				if (pos > 0) {//si el error se encuentra en el inicial, HACE FUNCION DE SACAR EN UNA CADENA 
					MensajeDeError += "Error de Sintaxis2: "+lex.getValor(pos)+" después de "+ lex.getValor(pos-1)+" en la línea "+ linea+"\n"
							+ "Se esperaba un "+pila.peek()+"\n";
					//pila.pop();//son diferentes, por esa razon sacamos 
					errP = false;
				}else {
					MensajeDeError += "Error de Sintaxis3: "+lex.getValor(pos)+" al inicio de la línea 1\n"
							+ "Se esperaba un "+pila.peek()+"\n";
					pila.pop();//son diferentes, por esa razon sacamos para que siga
					errP = false;
				}
				//iniciofinale(lex.getValor(pos));
				//entonces es un  no terminal
			}else {
				apila(terminales.indexOf(pila.peek()), elexe.indexOf(lex.getValor(pos)), pos);
			}
		}
	}
	
	//Aqui apila hasta lo indicado en procesoApilAndDesapil()
	public void apila(int i, int j, int pivote) {
		String interseccion = tabla1[i][j];
		System.out.println("interseccion "+tabla1[i][j]);
		System.out.println("posicion "+i+" , "+j);
		System.out.println("pivote"+pivote);
		if (interseccion == " " || interseccion.equals("saltar")) {
			if (pivote > 0) {
				MensajeDeError += "Error de Sintaxis2: "+lex.getValor(pivote)+" después de "+ lex.getValor(pivote-1)+" en la línea "+ linea+"\n"
						+ "Tipo de dato incorrecto\n" ; errP = false;
			}else {
				MensajeDeError += "Error de Sintaxis3: "+lex.getValor(pivote)+" al inicio de la línea 1\n"
						+ "Tipo de dato incorrecto\n" ; errP = false;
			}
			errP = false;
			MensajeDePila += pila+"\n";
		}else {
			String[] interseccionArray = interseccion.split(" ");
			String tempo=pila.peek();
			if (!((lex.getValor(pivote).equals("ciP")) && cerra == 0) || !((lex.getValor(pivote).equals("finale")) && cerra < 0)) {//el contador de parentesis
			pila.pop();
			for (int k = interseccionArray.length; k > 0; k--) {
				pila.push(interseccionArray[k - 1]);
			}
			if (pila.peek().equalsIgnoreCase("ç") ||pila.peek().equalsIgnoreCase("sacar")) {
				if((tempo.equalsIgnoreCase("T")||tempo.equalsIgnoreCase("E")||tempo.equalsIgnoreCase("F") ||tempo.equalsIgnoreCase("L")|| tempo.equalsIgnoreCase("R") || tempo.equalsIgnoreCase("Z"))) {
					MensajeDeError+="Error de Sintaxis: Se esperaba un operando despues de: "+ lex.getValor(pivote-1)+" en la línea "+ linea +"\n" ; 	
					}			
					pila.pop();
			}
			if (pila.peek().equalsIgnoreCase(lex.getValor(pivote))) { //encontro el terminal que busca
				MensajeDePila += pila+"\n";
				//iniciofinale(lex.getValor(pivote));
				pila.pop();
				MensajeDePila += pila+"\n";errP = true;
			} else { //hara un produce
				MensajeDePila += pila+"\n";
				procesoApilAndDesapil(pivote);
			}}else {
				MensajeDePila += pila+"\n";
				MensajeDeError += "Se cambio la accion de 'sacar' del "+lex.getValor(pivote)+" después de "+ lex.getValor(pivote-1)+" por la accion 'saltar' produciendo el siguiente mensaje: \n"; 	
				MensajeDeError += "Error de Sintaxis: "+lex.getValor(pivote)+" después de "+ lex.getValor(pivote-1)+" en la línea "+ linea +"\n" ; 	
				errP=false;
			}
		}
	}
}