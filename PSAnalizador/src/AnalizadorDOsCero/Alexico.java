package AnalizadorDOsCero;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alexico {

    ListaSencilla archivo = new ListaSencilla();
    TokenGenerator genToken = new TokenGenerator();
    int inicio, fin, linea;
    private boolean detener = false;
    private String mensajeError = "";
    int bande=0, nlinea=0, entraveces=0;
    private String token,tokenP;
    private boolean banderadoble=false, banderacomment=false ,banderanegaE=false, escero=false, primero=false;//vairables de entorno
    public boolean blanco=false;
    public Alexico(String filePath) {
    	this.pattern = null;
		try {
    		BufferedReader in = new BufferedReader(new FileReader(filePath));
    		String line = null;
    		while ((line = in.readLine()) != null) {
    				archivo.addValue(line);     //anade a lista de lineas 
    		}
    	}catch (IOException e) {
    		detener = true;
    		mensajeError += "Error en lectura de archivo: " + filePath;
    		return;
		}
    	if (archivo.isEmpty()) {
    		detener = true;
    		mensajeError += "El archivo está en blanco" + filePath;
    		return;
    	}
    	inicio=0;
    	fin=0;
    	linea=0;//inicio de linea
        siguiente();
    }
    String tAsign = "";
    String tAsigtemp= "";
    String cadena;
    boolean continuar = true;
    boolean cumple=true;
    int caso = 0;

    public void siguiente() {
    	 if (detener) {
             return;
         }
    	 if (archivo.listLenght() == linea) {//comprobamos que no tenga nada
 			detener = true;
 			return;
 		}
    	 if (!(archivo.listLenght() == linea)) {
    		 if (procesalex())
    			 return;
    	 }
    }
    void reinicia () {
    	linea++; //incrementamos linea
    	inicio=0;
    	fin=0;
    }
    public boolean procesalex() {
    	/*if (fin<cadena.length()) {
    		linea++;
    		inicio=0;
        	fin=0;
    	}*/
    	nlinea=0;
    	blanco=false;
        if (linea < archivo.listLenght()) { //Imprime en pantalla el archivo completo
            if (banderacomment == false) {
                cumple=true;
                tAsign="";
            }
            cadena = archivo.getValor(linea);
            System.out.println("Tamaño de la cadena es: " + cadena.length());
            System.out.println("linea: "+linea);
            do {
                switch (caso) {
                    case 0: //Engloba el analisis de un numero, sin errores aÃºn
                        if (fin >= cadena.length()) {
                        	reinicia();
                        	blanco=true;
                            return true;
                        }
                        if (cadena.charAt(fin) == ' ' || cadena.charAt(fin)== '\t' || cadena.charAt(fin)=='\r') {
                            fin++;
                            inicio = fin;
                            caso = 0;
                            blanco=true;//se marca la variable, en caso de que sea el unico valor en la linea o la tenga en el final
                            break;
                        }
                        try {
                        	if (cadena.charAt(fin)=='/' && cadena.charAt(fin+1)=='*') {
                            	caso=20;
                            	fin=fin+2;
                            	break;
                            }	
                        }catch(Exception e32) {//encontro solo el operador de div
                            caso = 5;
                            break;
                        }
                        

                        if (isMinus(cadena.charAt(fin))) { //Si encuentra una minuscula;
                            caso=8;
                            fin++;
                            break;
                        }
                        if (cadena.charAt(fin)=='@') {//caso de un identificador
                        	caso = 10;
                            fin++;
                            break;
                        }
                        if (cadena.charAt(fin)=='"') {//comentario
                        	caso = 11;
                        	fin++;
                        	break;
                        }
                        if (cadena.charAt(fin)=='\'') {
                        	caso = 14;
                        	fin++;
                        	break;
                        }
                        if (cadena.charAt(fin) == ':') {//operador de clase
                        	caso=16;
                        	fin++;
                        	break;
                        }
                        if (cadena.charAt(fin) == '#') { //Caso de comentario
                        	caso=18;
                        	fin++;
                        	break;
                        }
                        if (fin < cadena.length() && cadena.charAt(fin) >= '1' && cadena.charAt(fin) <= '9') {
                        		caso = 1; //Lo manda al comparador de numero
                                fin++;
                    		break;
                        }if(cadena.charAt(fin)=='0') {//caso de ceros    
                        	fin++;
                        	caso=23;
                        	break;
                        }//caso del nuemro negativo
                        if (cadena.charAt(fin)=='-') {
                        	caso=24;
                        	fin++;
                        	break;
                        }
                        //caracter simple
                        if (isCaracter(cadena.charAt(fin))) {
                            caso = 5;
                            break;
                        }
                        
                        else {
                        	tokenP="error";//indica el token
                            System.out.println("error generado en linea "+(linea+1));
                            fin++;
                            for (int i = inicio; i < fin; i++) {
                                tAsign = "" + tAsign + cadena.charAt(i);
                            }
                            nlinea=linea+1;
                            blanco=false;
                            inicio=fin;   
                            return true;
                        }

                    case 1: //Numeros enteros
                        if (fin < cadena.length() && cadena.charAt(fin) == '.' && isNum(cadena.charAt(fin + 1))) {
                            fin++; //Si hay un punto y es float
                            caso = 3;
                            break;
                        }
                        if(fin<cadena.length() && (isMayus(cadena.charAt(fin)) || isMinus(cadena.charAt(fin)) || cadena.charAt(fin)=='"'
                        	   || cadena.charAt(fin)=='$' || cadena.charAt(fin)=='¡' ||
                        		cadena.charAt(fin)=='¿' || cadena.charAt(fin)=='{' || cadena.charAt(fin)=='}' || cadena.charAt(fin)=='@' || cadena.charAt(fin)=='#')){
                            if (!(cadena.charAt(fin)==' ')) {//como en cadena se registra el espacio en blanco
                            	//ya no puede salir y con esta condicion puede seguir
                            	cumple=false;
                                fin++;
                                break;
                            }
                        }
                        
                        if (fin < cadena.length() && isNum(cadena.charAt(fin))) {
                            fin++;
                            break;
                        } else {
                            caso = 2; //Lo manda para generar un token del numero que encontro
                        }

                        ;
                        break;

                    case 2: //Genera el token de los numeros enteros. 
                    	
                    	
                        if(cumple==true){ //Cumple los requerimientos
                            if (banderanegaE==true) {//encontro un negativo
                            	token = "" + genToken.buscaTokenClasif("id_ent");
                            	tAsign+="-"; //UN MENOS
                                for (int i = inicio; i < fin; i++) {
                                    tAsign = "" + tAsign + cadena.charAt(i);
                                }
                                genToken.tiratoken();
                                genToken.guardaToken(tAsign + " , id_ent , " + token);
                                tokenP=genToken.buscaTokenPalabra(3, token.toString());
                                inicio = fin;
                                banderanegaE=false;
                                nlinea=linea+1;
                            }else {
                            	token = "" + genToken.buscaTokenClasif("id_ent");
                                for (int i = inicio; i < fin; i++) {
                                    tAsign = "" + tAsign + cadena.charAt(i);
                                }
                                genToken.guardaToken(tAsign + " , id_ent , " + token);
                                tokenP=genToken.buscaTokenPalabra(3, token.toString());
                                inicio = fin;
                                nlinea=linea+1;
                            }blanco=false;//pueda entrar al sintactico
                          
                        if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            //tAsign = "";
                            caso = 0;
                            return true;
                        } else {
                            continuar = false;
                            break;
                        }
                            
                        }else{ //No cumple con los requerimientos y no debe ser catalagoda con un token.
                            cumple=true;
                            for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                            inicio=fin;
                            genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                            tokenP="error";//indica el token
                            nlinea=linea+1;
                            blanco=false;//pueda entrar al sintactico
                            if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            //tAsign = "";
                            caso = 0;
                            return true;
                        } else {
                            continuar = false;
                        }
                        }
                        ;
                        break;

                    case 3: //Genera los numeros con punto flotante.
                    	if (fin < cadena.length()) {
                    		if (cadena.charAt(fin)=='.'||isMayus(cadena.charAt(fin)) || isMinus(cadena.charAt(fin))) {
                    			cumple=false; //Si contiene alguna lentra entonces no aceptar la palabra
                                fin++;
                                break;
                    		}else if (isNum(cadena.charAt(fin))) {
                    			if (banderanegaE==true && cadena.charAt(fin)=='0') {
                    				caso=26;
                    				cumple=false;
                    			}
                                fin++; //Analizar si es un numero

                            } else { //Si llego al ultimo caso o no es un numero entonces analizar.
                            	if (cadena.charAt(fin-1)=='.')
                            		cumple=false;
                                caso = 4;
                               
                            }
                    	}else {
                    		if (cadena.charAt(fin-1)=='.')
                        		cumple=false;
                    		caso=4;
                    	}
                        break;

                    case 4: //Asigna el token a la clasificacion de los flotantes.
                        if(cumple==true){
                            token = "" + genToken.buscaTokenClasif("id_dec");
                        for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                        genToken.guardaToken(tAsign + " , id_dec , "+ token);
                        tokenP=genToken.buscaTokenPalabra(3, token.toString());
                        inicio = fin;
                        nlinea=linea+1;
                        blanco=false;//pueda entrar al sintactico
                        if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            tAsign = "";
                            caso = 0;
                            return true;
                        } else {
                            continuar = false;
                        }
                        
                        }else{ //No cumple con los requerimientos del token
                            for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                            nlinea=linea+1;
                            inicio=fin;
                            genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                            tokenP="error";//indica el token
                            blanco=false;//pueda entrar al sintactico
                            if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            //tAsign = "";
                            caso = 0;
                            cumple=true;
                            return true;
                        } else {
                            continuar = false;
                        }
                        }
                        ;
                        break;
                    case 5: // Asigna un valor de caracter
                    	if (isCaracterDouble(cadena.charAt(fin))) {
                    		if (banderadoble==true)//hay un caracter doble antes
                    		{
                    			if (!(cadena.charAt(fin)=='|' || cadena.charAt(fin)=='?' || cadena.charAt(fin)=='=')) {
                    				token = "" + genToken.buscaTokenCar("" + cadena.charAt(fin));
                                    tAsign = "" + cadena.charAt(fin);
                                    genToken.guardaToken(tAsign + " , "+genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin))+" , " + token);
                                    tokenP=genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin));
                    			}else {//error de caracter
                    				 caso=27;
                    				 fin++;
                    				 break;
                    				/*tAsign = "" + tAsign + cadena.charAt(fin);
                                     genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                                     tokenP="error";//indica el token
                                     bande=0;*/
                    			}
                    			fin++;
                                inicio = fin;
                                nlinea=linea+1;
                                banderadoble=false;
                                blanco=false;//pueda entrar al sintactico
                                if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                                    caso = 0;
                                    return true;
                                } else {
                                    continuar = false;
                                }
                    		}else {
                    			caso=27; //se va aun caso de dobles
                        		fin++;
                    		}break;
                    	}else {//es un caracter pero no pertenece a un doble
                    		token = "" + genToken.buscaTokenCar("" + cadena.charAt(fin));
                            tAsign = "" + cadena.charAt(fin);
                            genToken.guardaToken(tAsign + " , "+genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin))+" , " + token);
                            tokenP=genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin));
                            fin++;
                            inicio = fin;
                            nlinea=linea+1;
                            banderadoble=false;
                            blanco=false;//pueda entrar al sintactico
                            if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                                caso = 0;
                                return true;
                            } else {
                                continuar = false;
                               
                            }
                    	}
                    	break;
                    	
                    
                    case 6://caso del identificador
//                        System.out.println(cadena.charAt(fin));
                        if (fin < cadena.length()) { //Si es menor al tamaño de la cadena entonces compara
                            if (cadena.charAt(fin) == '_') { //Compara con un guion bajo
                                fin++;
                                break;
                            }
                            if (isMayus(cadena.charAt(fin))) { //Si es mayuscula entonces ya no cumple los requisitos
                                fin++;
                                cumple=false;
                                break;
                            }
                            if (isMinus(cadena.charAt(fin)) || isNum(cadena.charAt(fin))) { //Si es un numero o minuscula
                                fin++;
                                break;
                            } else { //Si fin es mayor o igual a la cadena entonces terminar el analizis.
                                caso = 7;
                            }
                            
                        } else { //Si no es menor, entonces mandamos de una vez a que guarde los tokens
                            caso = 7;
                        }

                        ;
                        break;
                    case 7: //Genera el token del Identificador
                        if (cadena.charAt(fin-1) == '_'){ //Si al final encuentra un guion bajo entonces no cumple
                        cumple=false;
                        }
                            
                        if(cumple==true){ //Si cumple los requisitos de ser un Identificador.
                        	token="";
                          if (bande==1) {
                          	token = "" + genToken.buscaTokenClasif("identificador");
                          	tokenP=genToken.buscaTokenPalabra(3, token.toString());
                          }else if (bande==2){
                          	token = "" + genToken.buscaTokenClasif("id_clase");
                          	tokenP=genToken.buscaTokenPalabra(3, token.toString());
                          }else if (bande==3) {
                          	token = "" + genToken.buscaTokenClasif("id_func");
                          	tokenP=genToken.buscaTokenPalabra(3, token.toString());
                          }

                        for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                        genToken.guardaToken(tAsign +" , "+genToken.buscaTokenPalabra(3, token.toString())+" , " + token);
                        bande=0;//reinicia la bandera
                        inicio = fin; 
                        nlinea=linea+1;
                        }else{ //Si no cumple con los requisitos entonces genera un error
                           for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }   
                         inicio=fin;
                         nlinea=linea+1;
                         genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                         tokenP="error";//indica el token
                         bande=0;
                         
                        }blanco=false;//pueda entrar al sintactico
                        if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                            cumple=true;
                            //tAsign = "";
                            caso = 0;
                            return true;
                        } else {
                            continuar = false;
                        }
                        
                        break;
                    
                    case 8: //Analiza todas las palabras con minusculas hasta que encuentre algo diferente
                        if(fin<cadena.length()){
                            if(isMinus(cadena.charAt(fin))){ //Completa el ciclo de la cadena de minusculas
                            fin++;
                        }else{
                                caso=9;
                            }
                        }else{
                            caso=9;
                        }
                        ;break;
                    
                    case 9: //Caso que genera el token de la palabra reservada o un error, en caso de que no sea una
                        for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                        if(isReserv(tAsign)){ //Es una palabra reservada, entonces le genera un token.
                            token=""+genToken.buscaTokenP(tAsign);
                            genToken.guardaToken(tAsign+" , "+genToken.buscaTokenPalabra(1, tAsign)+" , "+token);
                            tokenP=genToken.buscaTokenPalabra(1, tAsign);
                        }else{ //No es una palabra reservada
                        	genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                        	tokenP="error";//indica el token
                        }
                        nlinea=linea+1;
                        inicio=fin; //Mueve el inicio al fin
                        blanco=false;//pueda entrar al sintactico
                        if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            //tAsign = "";
                            caso = 0;
                            return true;
                        } else {
                            continuar = false;
                        }
                        ;break;
                    case 10: //IDENTEIFICADOR de cadena checa si la palabra es mayor o menor
                    	if (fin < cadena.length()) {
                    		if (isMayus(cadena.charAt(fin))) { //Si encuentra una mayuscula para analizarla como Identificador
                                caso = 6;
                                fin++;
                                bande=1;
                                break;
                            }else if (cadena.charAt(fin)=='@' || cadena.charAt(fin)==':' || isNum(cadena.charAt(fin)) ||isMinus(cadena.charAt(fin))) {//|| isCaracter(cadena.charAt(fin))
                            	//if (isCaracter(cadena.charAt(fin)) && (cadena.charAt(fin+1)!=':'))
                            	cumple=false; //ya no cumplio
                            	fin++;
                            	break;
                            }
                            else {//ya no identifico burradas
                           	 caso=7;
                           	 cumple = false;
                           	 break;
                            }
                    	}else {
                    		caso=7; //solo encontro un @ o una palabra en minus
                    		cumple=false;
                    	}break;
                    case 11:
                    		if(fin<cadena.length()){
              
                                if(isMinus(cadena.charAt(fin))){ //Completa el ciclo de la cadena de minusculas
                                fin++;
                                break;
                                }if (isMayus(cadena.charAt(fin))) {
                                fin++;
                                break;}
                                if (isCad(cadena.charAt(fin)) || cadena.charAt(fin)=='\'') {
                                fin++;
                                break;}
                                if (isNum(cadena.charAt(fin))) {
                                fin++;
                                break;
                                }
                                if (cadena.charAt(fin)=='\n' || cadena.charAt(fin)=='\r' || cadena.charAt(fin)=='\t') {
                                	fin++;
                                    break;
                                }
                                else {
                                	fin++;
                                	caso=12; //encontro un "
                                	break;
                                }
                                	
                            }else{
                               caso=12;
                            }break;
                    case 12:
                    	if (isMinus(cadena.charAt(fin-1)) || isMayus(cadena.charAt(fin-1)) || isCad(cadena.charAt(fin-1)) 
                    			|| cadena.charAt(fin-1)==';'|| isNum(cadena.charAt(fin-1)) || cadena.charAt(fin-1)=='\n' || cadena.charAt(fin-1)=='\r' || cadena.charAt(fin-1)=='\t'){ //Si al final encuentra un guion bajo entonces no cumple
                            cumple=false;
                            }
                                
                            if(cumple==true && cadena.charAt(fin-1)=='"'){ 
                            	token = "" + genToken.buscaTokenClasif("id_cad");

                            for (int i = inicio; i < fin; i++) {
                                tAsign = "" + tAsign + cadena.charAt(i);
                            }
                            tokenP=genToken.buscaTokenPalabra(3, token.toString());
                            genToken.guardaToken(tAsign + " , "+tokenP+" , " + token);
                            inicio = fin;
                            nlinea=linea+1;
                            }else{ //Si no cumple con los requisitos entonces genera un error
                               for (int i = inicio; i < fin; i++) {
                                tAsign = "" + tAsign + cadena.charAt(i);
                            }   
                             inicio=fin;
                             nlinea=linea+1;
                             System.out.println("Error:"+tAsign + ", error lexico linea "+ (linea+1));
                             genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                             tokenP="error";//indica el token
                            }blanco=false;//pueda entrar al sintactico
                            if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                cumple=true;
                                //tAsign = "";
                                caso = 0;
                                return true;
                            } else {
                                continuar = false;
                            }
                            
                            break;
                    case 13: // Asigna un valor de caracter
            
                        token = "" + genToken.buscaTokenCar(""+cadena.charAt(fin-1)+cadena.charAt(fin));
                        tAsign = "" + cadena.charAt(fin-1) + cadena.charAt(fin);
                        tokenP=genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin-1)+cadena.charAt(fin));
                        genToken.guardaToken(tAsign + " , "+tokenP+" , " + token);
                        fin++;
                        inicio = fin;
                        nlinea=linea+1;
                        blanco=false;//pueda entrar al sintactico
                        if (fin < cadena.length()) { //Si la cadena ya llego a su fin
                            //tAsign = "";
                            caso = 0;
                            return true;
                        } else {
                            continuar = false;
                        }

                        ;
                        break;
                    case 14: //caso id_caracter
                    	if(fin<cadena.length()){
                            if(isMinus(cadena.charAt(fin))){ //Completa el ciclo de la cadena de minusculas
                            fin++;
                            caso=15;
                            break;
                            }if (isMayus(cadena.charAt(fin))) {
                            caso=15;
                            fin++;
                            break;}
                            if (isCad(cadena.charAt(fin))) {
                            fin++;
                            caso=15;
                            break;}
                            if (isNum(cadena.charAt(fin))) {
                            fin++;
                            caso=15;
                            break;
                            }
                            if (cadena.charAt(fin)=='"' || cadena.charAt(fin)==';'){
                            	fin++;
                                caso=15;
                                break;	
                            }
                            else {
                            	fin++;
                            	caso=15; //encontro un "
                            	break;
                            }
                            	
                        }else{
                        	cumple=false;
                        	//aqui seria un error xD
                           caso=15;
                        }break;
                    case 15: //id de caracteres

                    		if (cumple==true) {
                    			if(cadena.charAt(fin)=='\'') {
                    				fin++;//podemos decir que ya estuvo
                    				token = "" + genToken.buscaTokenClasif("id_cart");
                                    for (int i = inicio; i < fin; i++) {
                                        tAsign = "" + tAsign + cadena.charAt(i);
                                    }
                                    tokenP=genToken.buscaTokenPalabra(3, token.toString());
                                    genToken.guardaToken(tAsign + " , "+tokenP+" , " + token);
                                    inicio = fin;
                                    nlinea=linea+1;
                    			}else { //se genera un error
                    				fin++;
                    				cumple=false;    
                    				break;
                    			}
                    			blanco=false;//pueda entrar al sintactico
                    			if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                    cumple=true;
                                    //tAsign = "";
                                    caso = 0;
                                    return true;
                                } else {
                                    continuar = false;
                                }
                    		}else {//se encuentra como que no llego a algo
                    			fin++;
                    			for (int i = inicio; i < fin; i++) {
                                    tAsign = "" + tAsign + cadena.charAt(i);
                                } 
                    			genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                    			tokenP="error";//indica el token
                                       inicio = fin;
                                       nlinea=linea+1;
                                       blanco=false;//pueda entrar al sintactico
                                 if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                     cumple=true;
                                     tAsign = "";
                                     caso = 0;
                                     return true;
                                 } else {
                                     continuar = false;
                                 }
                    		}	
               	
                    	break;
                    case 16: //identficador de clase 
                    	if (fin<cadena.length()) {
                    		if (cadena.charAt(fin)==':') {
                    			fin++;
                    			caso=17;
                    			break;
                    		}
                    		if (isMayus(cadena.charAt(fin))) { //Si encuentra una mayuscula para analizarla como Identificador
                                caso = 6;
                                fin++;
                                bande=2;
                                break;
                            }
                    		if (cadena.charAt(fin)==':' || isMayus(cadena.charAt(fin)) || isNum(cadena.charAt(fin)) || isCaracter(cadena.charAt(fin)) ||isMinus(cadena.charAt(fin))) {
                           	 cumple=false;
                           	 fin++;
                           	 break;
                            }else {
                            	caso=7;
                           	 cumple = false;

                            }

                    	}else {
                    		cumple=false;
                    		caso=7;
                    	}
                    	break;
                    case 17: //id de clase
                    	if (fin< cadena.length()) {
                    		if (isMayus(cadena.charAt(fin))) { //Si encuentra una mayuscula para analizarla como Identificador
                                caso = 6;
                                fin++;
                                bande=3;
                                break;}
                             if (cadena.charAt(fin)==':' || isMayus(cadena.charAt(fin))||isNum(cadena.charAt(fin)) || isCaracter(cadena.charAt(fin)) ||isMinus(cadena.charAt(fin))) {
                            	 cumple=false;
                            	 fin++;
                            	 break;
                             }else {
                            	 caso=7;
                            	 cumple = false;
                             }
                            }else {
                            	caso=7;
                           	 cumple = false;

                            }
                    	break; 
                    case 18:
                    	if (fin<cadena.length()) {
                    		if (isNum(cadena.charAt(fin))|| isMayus(cadena.charAt(fin))|| 
                    				isMinus(cadena.charAt(fin)) || isCaracter(cadena.charAt(fin))
                    				|| isCad(cadena.charAt(fin))) {//casos posibles
                    			fin++;
                    			caso=19;
                    			break;
                    		}else {
                    			fin++;//no se ira de este caso hasta el final de la cadena
                    			break;
                    		}
                    	}else {//caso error se quedo con un #
                    		for (int i = inicio; i < fin; i++) {
                                tAsign = "" + tAsign + cadena.charAt(i);
                            } 
                    		genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                    		tokenP="error";
                    		nlinea=linea+1;
                                   inicio = fin;
                                   blanco=false;//pueda entrar al sintactico
                                   //tAsign = "";
                                   continuar = false;
                    	}
                    	break;
                    case 19: 
                    	if (fin<cadena.length()) {
                    		if (isNum(cadena.charAt(fin))|| isMayus(cadena.charAt(fin))|| 
                    				isMinus(cadena.charAt(fin)) || isCaracter(cadena.charAt(fin))
                    				|| isCad(cadena.charAt(fin)) || cadena.charAt(fin)=='@'
                    				|| cadena.charAt(fin)=='_' || cadena.charAt(fin)=='/' ||
                    				cadena.charAt(fin)=='\t' || cadena.charAt(fin)=='\r') {//casos posibles
                    			fin++; //se matendra en el mismo caso
                    			break;
                    		}
                    	}else {//al ser un comentario solo se termina hasta haber un final de linea
                    		token = "" + genToken.buscaTokenClasif("comment");
                            for (int i = inicio; i < fin; i++) {
                                tAsign = "" + tAsign + cadena.charAt(i);
                            }
                            nlinea=linea+1;
                            tokenP=genToken.buscaTokenPalabra(3, token.toString());
                            genToken.guardaToken(tAsign + ", "+tokenP+", " + token);
                            inicio = fin;
                            blanco=false;//pueda entrar al sintactico
                            continuar = false;
                    	}
                    	break;
                    case 20: //caso comentario abierto
                    	if (fin<cadena.length()) {
                    	if (cadena.charAt(fin)=='*'){//pasa al siguente estado
                    				caso=21;
                    				fin++;
                    				break;
                    	}
                    		if (isNum(cadena.charAt(fin))|| isMayus(cadena.charAt(fin))|| 
                    				isMinus(cadena.charAt(fin)) || isCaracter(cadena.charAt(fin))
                    				|| isCad(cadena.charAt(fin)) || cadena.charAt(fin)=='@'
                    				|| cadena.charAt(fin)=='_' ||
                    				cadena.charAt(fin)=='\t' || cadena.charAt(fin)=='\r') {//casos posibles
                    			fin++; //se queda en el mismo estado 
                    			break;
                    		}
                    	}else {
                    		//se pasa a la siguiente linea haca el mismo caso hasta que encuentre el temrinador
                    		continuar=false;
                    		banderacomment=true;
                    		//caso=22;
                    		for (int i = inicio; i < fin; i++) {
                                tAsigtemp+= ""+cadena.charAt(i);
                            }
                    		tAsigtemp+="\n";
                    		tAsign+=tAsigtemp;
                    		blanco=false;//pueda entrar al sintactico
                    		if ((linea+1) == archivo.listLenght()) {//paso el umbral de deteccion
                    			banderacomment=false; //reinicia la bendera
                                inicio=fin;
                                genToken.generaError(tAsigtemp + ", error lexico linea "+ (linea+1));
                                tAsign=tAsigtemp;
                                nlinea=linea+1;
                                linea++;
                                tokenP="error";
                                return true;
                    	}else{
                    		continuar=false;
                    	}
                    		}
                    	break;
                    case 21: //lo mismo que el 20
                    	if (fin<cadena.length()) {
                    		if (cadena.charAt(fin)=='/') {
                				caso=22;
                				fin++;
                				break;
                			}
                    		if (isNum(cadena.charAt(fin))|| isMayus(cadena.charAt(fin))|| 
                    				isMinus(cadena.charAt(fin)) || isCaracter(cadena.charAt(fin))
                    				|| isCad(cadena.charAt(fin)) || cadena.charAt(fin)=='@'
                    				|| cadena.charAt(fin)=='_' ||
                    				cadena.charAt(fin)=='\t' || cadena.charAt(fin)=='\r') {//casos posibles
                    			fin++; //se queda en el mismo estado 
                    			break;
                    		}
                    	}else {
                    		//se pasa a la siguiente linea haca el mismo caso hasta que encuentre el temrinador
                    		continuar=false;
                    		banderacomment=true;
                    		//caso=22;
                    		for (int i = inicio; i < fin; i++) {
                                tAsigtemp= "" + tAsigtemp + cadena.charAt(i);
                            }
                    		tAsigtemp+="\n";
                    		tAsign+=tAsigtemp;
                    		blanco=false;//pueda entrar al sintactico
                    		if ((linea+1) == archivo.listLenght()) {//paso el umbral de deteccion
                    			banderacomment=false; //reinicia la bendera
                                inicio=fin;
                                genToken.generaError(tAsigtemp + ", error lexico linea "+ (linea+1));
                                tAsign=tAsigtemp;
                                nlinea=linea+1;
                                linea++;
                                tokenP="error";
                                return true;
                    	}else{
                    		continuar=false;
                    				}
                    	}
                    	break;
                    case 22: //parecido al caso 7
                    	if (inicio>1) {//inicio con otra linea
                    		if (cadena.charAt(fin-1) == '/' && cadena.charAt(fin-2)=='*'){ //por si llego al fi
                                cumple=true; 
                                }
                    	}      
                            if(cumple==true){ //Si cumple los requisitos de ser comentario
                            	token="";
                            	token = "" + genToken.buscaTokenClasif("ini_com"); 
                            for (int i = inicio; i < fin; i++) {
                                tAsigtemp = "" + tAsigtemp + cadena.charAt(i);
                            }
                            genToken.guardaToken(tAsigtemp + " , ini_com , " + token);
                            tAsign=tAsigtemp;
                            tokenP="ini_com";
                            nlinea=linea+1;
                            inicio = fin;
                            }else{ //Si no cumple con los requisitos entonces genera un error
                             banderacomment=false; //reinicia la bendera
                             inicio=fin;
                             nlinea=linea+1;
                             genToken.generaError(tAsigtemp + ", error lexico linea "+ (linea+1));
                             tAsign=tAsigtemp;
                             tokenP="error";
                            }
                            blanco=false;//pueda entrar al sintactico
                            if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                cumple=true;
                                //tAsign = "";
                                caso = 0;
                                return true;
                            } else {
                                continuar = false;
                            }
                    	break;
                    case 23: //caso de ceros
                    	entraveces++;
                    		if (fin < cadena.length()) {//entero a decimal
                    			if (cadena.charAt(fin)=='.' && primero==false) {
                    				fin++;
                    				caso=3; //decimal
                    				cumple=true; //ya no es -0
                    				entraveces=0;
                    				break;
                    			}
                    			primero=true;//ya no puede pasar con el decimal
                    			if (cadena.charAt(fin)=='0') {
                    				fin++;
                    				escero=true;
                    				break;
                    			}if (isNum2(cadena.charAt(fin))) {
                    				inicio=fin;//para que solo marque los numeros
                    				fin++;
                    				cumple=true; //llego a un num al final
                    				caso=1;
                    				entraveces=0;
                    				primero=false;
                    				break;
                    			}if (fin<cadena.length() && (isMayus(cadena.charAt(fin)) || isMinus(cadena.charAt(fin)) || cadena.charAt(fin)=='"'
                    					|| cadena.charAt(fin)=='_' || cadena.charAt(fin)==',' || cadena.charAt(fin)=='?' || cadena.charAt(fin)=='$' || cadena.charAt(fin)=='¡' ||
                             		cadena.charAt(fin)=='¿' || cadena.charAt(fin)=='{' || cadena.charAt(fin)=='}' || cadena.charAt(fin)=='@' || cadena.charAt(fin)=='#')
                    					|| cadena.charAt(fin)=='.' || cadena.charAt(fin)=='[' || cadena.charAt(fin)==']'){
                                 
                    				if (!(cadena.charAt(fin)==' ')) {//como en cadena se registra el espacio en blanco
                                 	//ya no puede salir y con esta condicion puede seguir
                                 	//tAsign+=""+cadena.charAt(fin);
                                	 cumple=false;
                                     fin++;
                                     escero=false;
                                     caso=25;
                                 	entraveces=0;
                                     break;
                                 }
                    			}else {//llego un caracter simple o un ;
                    				if(escero==true) {//todos los casos son 0
                    					if (banderanegaE==true) {//no hay -0
                        					fin++;
                        					primero=false;
                        					caso=2;
                        					entraveces=0;
                    					}else {
                    						inicio=fin;
                        					fin++;
                        					cumple=true;
                        					primero=false;
                        					caso=2;
                        					entraveces=0;
                    					}
                    					banderanegaE=false;
                    					break;
                    				}else {
                    					if((cumple==false && banderanegaE==true)|| entraveces>1) {
                    						//se va al error con todo y negativo
                        					fin++;
                        					caso=2;
                        					entraveces=0;
                    					}
                    					else {
                    						fin++;
                        					caso=2;
                        					entraveces=0;
                    					}
                    					primero=false; break;
                    				}
                    			}
                    		}else {//paso el elemento, si tenia un negativo marca error
                    				banderanegaE=false;
                    				caso=2;
                    				primero=false;
                    				entraveces=0;
                    				break;
                    			}
                    case 24://op_res o numeros negatvos
                    	if (fin < cadena.length()) {
                    		if (isNum(cadena.charAt(fin))){
                    			if (!(cadena.charAt(fin)=='0')) {
                    				fin++;
                        			caso=1; //caso de los enteros
                        			banderanegaE=true;
                    			}else {
                    				fin++;
                        			caso=23; //caso de ceros
                        			entraveces=0;
                        			cumple=false; //hay un -0
                    				banderanegaE=true;
                    			}
                    			break;
                    		}else if (cadena.charAt(fin)=='>'){ 
                           		   caso=13; /*encontro un igual*/
                           		   break;
                    		}
                    		else if (isCad(cadena.charAt(fin)) || isMinus(cadena.charAt(fin)) || isMayus(cadena.charAt(fin))) {
                    			fin--; //encontro un caracter simple
                    			caso=5;
                    			break;
                    		}
                    		else { //llego a un espacio o tablulador
                    			caso=5;
                    			fin--; 
                    		}
                    	}else {//es un op_res y llego al final de la linea
                    		caso=5;
                    		fin--;//decrementamos el fin para que busque
                    		break;
                    	}
                    	break;
                    case 25://caso de error 
                    	if (fin < cadena.length()) {
                    		if (isNum(cadena.charAt(fin)) || isMayus(cadena.charAt(fin)) || isMinus(cadena.charAt(fin)) || cadena.charAt(fin)=='"'
                					|| cadena.charAt(fin)=='[' || cadena.charAt(fin)==']' || isCaracter(cadena.charAt(fin))){
                    					fin++;
                    	}else {
                    		fin++;
                    		caso=2;
                    		break;
                    	}
                    	}else {
                    		caso=2;
                    	}break;
                    case 26: //entro a un negativo -0.0
                    	if (fin < cadena.length()) {
                    	
                    		if (cadena.charAt(fin)=='.'||isMayus(cadena.charAt(fin)) || isMinus(cadena.charAt(fin))) {
                    			cumple=false; //Si contiene alguna lentra entonces no aceptar la palabra
                                fin++;
                                break;
                    		}else if (isNum(cadena.charAt(fin))) {
                    				if (!(cadena.charAt(fin)=='0') && entraveces==0) {
                    					cumple=true;
                    					entraveces=1;
                    				}
                                fin++; //Analizar si es un numero

                            } else { //Si llego al ultimo caso o no es un numero entonces analizar.
                                caso = 4;
                                entraveces=0;
                            }
                    	}else {
                    		entraveces=0;
                    		caso=4;
                    	}
                        break;
                    case 27: //caso de caracteres
                    	if (fin < cadena.length() || cadena.charAt(fin)==' ' || cadena.charAt(fin)=='\t' || cadena.charAt(fin)=='\r') {
                    		if (Confirm_double_3(cadena.charAt(fin))) {//efectivamente que sea uno de los dobles
                    			token = "" + genToken.buscaTokenCar("" + cadena.charAt(fin-1)+cadena.charAt(fin));//numero de token
                    			for (int i = fin-1; i <= fin; i++) {
                                    tAsign = "" + tAsign + cadena.charAt(i);
                                    System.out.println("doble: "+cadena.charAt(i));
                                }
                    			genToken.guardaToken(tAsign + " , "+genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin-1)+cadena.charAt(fin))+" , " + token);
                                tokenP=genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin-1)+cadena.charAt(fin));
                                fin++;
                                inicio = fin;
                                nlinea=linea+1;
                                banderadoble=true;
                                
                    		}else { //no es un caso de ese tipo
                    			fin--;
                    			if (!(cadena.charAt(fin)=='|' || cadena.charAt(fin)=='?' || cadena.charAt(fin)=='=')) {
                    				token = "" + genToken.buscaTokenCar("" + cadena.charAt(fin));
                                    tAsign = "" + cadena.charAt(fin);
                                    genToken.guardaToken(tAsign + " , "+genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin))+" , " + token);
                                    tokenP=genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin)); 
                    			}else {//error de caracter
                    				for (int i = inicio; i <= fin; i++) {
                                        tAsign = "" + tAsign + cadena.charAt(i);
                                    }   
                                     genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                                     tokenP="error";//indica el token
                                     bande=0;
                    			}
                    			fin++;
                                inicio = fin;
                                nlinea=linea+1;
                                banderadoble=false;
                                
                    	}
                    		blanco=false;//pueda entrar al sintactico
                            if (fin < cadena.length()) { //Si la cadena ya llego a su fin, no volver a entrar
                                caso = 0;
                                return true;
                            } else {
                                continuar = false;
                            }}else {//es un caracter simple
                    		fin--;//se recorre uno
                    		if (!(cadena.charAt(fin)=='|' || cadena.charAt(fin)=='?' || cadena.charAt(fin)=='=')) {
                				token = "" + genToken.buscaTokenCar("" + cadena.charAt(fin));
                                tAsign = "" + cadena.charAt(fin);
                                genToken.guardaToken(tAsign + " , "+genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin))+" , " + token);
                                tokenP=genToken.buscaTokenPalabra(2, ""+cadena.charAt(fin)); 
                			}else {//error de caracter
                                    tAsign = "" + tAsign + cadena.charAt(fin);
                                 genToken.generaError(tAsign + ", error lexico linea "+ (linea+1));
                                 tokenP="error";//indica el token
                                 bande=0;
                			}
                			fin++;
                            inicio = fin;
                            nlinea=linea+1;
                            banderadoble=false;
                            blanco=false;//pueda entrar al sintactico
                        if (fin < cadena.length()) { //Si la cadena ya llego a su fin, no volver a entrar
                            caso = 0;
                            return true;
                        } else {
                            continuar = false;
                            break;
                        }                    	
                }                    	
                 break;//no se regresa a 5 por que se enviara a un buclé
                }
           }while (continuar==true);
            if (banderacomment==true) {
            	reinicia();
            	continuar = true;
                bande=0;
            	procesalex();
            }else {
            	 caso = 0;
            	System.out.println("");
                System.out.println("Analisis de la linea: "+(linea+1));
                System.out.println("Errores generados. ");
                genToken.imprimeError();
                reinicia();
                continuar = true;
                bande=0;
                return true;
            }return true; 
        }else {
        	return false; //en caso que ya terminara
        }
    }
    private final Pattern pattern;
    private char c[]= {'<','>',' ','|','@','=','/','*','+','-','.','{','}',',','¡','!','?','¿','#','(',')','[',']','_',':'};
    public boolean isCad(char cad) {
    	boolean esCad=false;
    	for (int i = 0; i < c.length; i++) {
			if (c[i]==cad) {
				esCad=true;
				break;
			}
		}
    	return esCad;
    }

    public boolean isNum2(char cad) { //Verifica si es un numero el que se encuentra
        boolean esNum = false;
        if (cad >= '1' && cad <= '9') {
            esNum = true;
        }
        return esNum;
    }
    public boolean isNum(char cad) { //Verifica si es un numero el que se encuentra
        boolean esNum = false;
        if (cad >= '0' && cad <= '9') {
            esNum = true;
        }
        return esNum;
    }
    private char operadores[]= {'>','<','=','!','|',':',';','+','*','/','(',')',';','&',',','?'};//el menos se incluye como un caso especial
    public boolean isCaracter(char cad) { //Verifica si es un caracter especial
        boolean esCar = false;
        for (int i = 0; i < operadores.length; i++) {
        	if(operadores[i]==cad) {
        		esCar=true;
        		break;
        	}
		}
        return esCar;
    }
    public boolean isCaracterDouble (char cad){
    	boolean esCar2=false;
    	for (int i = 0; i < 5; i++) {
        	if(operadores[i]==cad ) {
        		esCar2=true;
        		break;
        	}else if (cad=='?') {
        		esCar2=true;
        	}
		}
    	return esCar2;
    }
    char []cdouble= {'=','|',':','!'};
    public boolean Confirm_double_3 (char cad) {
    	boolean esCar3= false;
    	for (int i = 0; i < cdouble.length; i++) {
			if (cdouble[i]==cad) {
				esCar3=true;
				break;
			}
		}
    	return esCar3;
    }

    public boolean isMayus(char cad) {
        boolean esMayus = false;
        if (cad >= 'A' && cad <= 'Z') {
            esMayus = true;
        }
        return esMayus;
    }

    public boolean isMinus(char cad) {
        boolean esMinus = false;
        if (cad >= 'a' && cad <= 'z') {
            esMinus = true;
        }

        return esMinus;
    }
    String reserve[]= {"si",
			"para",
			"sino",
			"contra",
			"mientras",
			"hacer",
			"ala",
			"mod",
			"tallo",
			"ruptura",
			"tallo",
			"caso",
			"principal",
			"ejecuta",
			"princpal",
			"ejecuta",
			"default",
			"ent",
			"dec",
			"cad",
			"cart",
			"bool",
			"inicio",
			"final",
			"verdadero",
			"falso",
			"crear",
			"funcion",
			"clase","imprime","lectura","retorna","dec","ent","cad","cart","proc"};
    public boolean isReserv(String cad){
        boolean esReserv = false;
        for (int i = 0; i < reserve.length; i++) {
			if(reserve[i].equals(cad)) {
				esReserv=true;
				break;
			}
		}
        return esReserv;
        }
    public String currentToken() {
        return tokenP;
    }
    public String currentLexema() {
        return tAsign;
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