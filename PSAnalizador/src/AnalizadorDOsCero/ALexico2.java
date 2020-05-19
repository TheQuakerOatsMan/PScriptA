package AnalizadorDOsCero;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALexico2 {

    ListaSencilla archivo = new ListaSencilla();
    TokenGenerator genToken = new TokenGenerator();
    int inicio, fin, lene;
    ListaSencilla lineas = new ListaSencilla (); 
    ListaSencilla complete = new ListaSencilla();
    private boolean detener = false;
    private String mensajeError = "";
    int bande=0;
    private boolean banderadoble=false, banderacomment=false;

    public ALexico2(String filePath) {
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
    	
        procesaLex();

    }

    public void procesaLex() {
        String token;
        String tAsign = "";
        String tAsigtemp= "";
        String cadena;
        boolean continuar = true;
        boolean cumple=true;
        int caso = 0;
        inicio = 0;
        fin = 0;
        for (int linea = 0; linea < archivo.listLenght(); linea++) { //Imprime en pantalla el archivo completo
            if (banderacomment == false) {
                cumple=true;
            }
            cadena = archivo.getValor(linea);
            System.out.println("Tamaño de la cadena es: " + cadena.length());
            do {
                switch (caso) {
                    case 0: //Engloba el analisis de un numero, sin errores aÃºn
                        if (fin >= cadena.length()) {
                            continuar = false;
                            break; 
                        }

                        if (cadena.charAt(fin) == ' ' || cadena.charAt(fin)== '\t' || cadena.charAt(fin)=='\r') {
                            fin++;
                            inicio = fin;
                            caso = 0;
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

                        // SI SALE UN O TENGO QUE HACER UN CASO EN ESPECIFICO CON UN TRY CATCH PARA COMPARAR SI ES UN SOLO 0 DEBIDO A QUE SALE
                        if (fin < cadena.length() && cadena.charAt(fin) >= '1' && cadena.charAt(fin) <= '9') {
                            caso = 1; //Lo manda al comparador de numero
                            fin++;
                            break;
                        }
                        try {
                            if (cadena.charAt(fin) == '0' && isNum(cadena.charAt(fin + 1))) {
                                fin++;
                                inicio=fin;
                                caso = 1;
                                break;
                            }
                            if (cadena.charAt(fin) == '0' && cadena.charAt(fin + 1) == '.' && isNum(cadena.charAt(fin + 2))) {
                                fin = fin + 2;
                                caso = 3;
                                break;
                            }
                        } catch (Exception e) {
                            fin++;
                            caso = 2;
                            break;
                        }
                        
                        try {
                      	   if (cadena.charAt(fin)=='-' && cadena.charAt(fin + 1)=='>') {
                      		   caso=13;
                      		   fin++;
                      		   break;
                      	   }
                         }catch(Exception e3) {
                      	   fin++;
                             caso = 5;
                             break;
                         }
                        if (isCaracter(cadena.charAt(fin))) {
                            caso = 5;
                            break;
                        }
                        
                        else {
                            genToken.generaError("Error en: " + cadena.charAt(fin));
                            fin++;
                            inicio=fin;
                            
                            break;
                        }

                    case 1: //Numeros enteros
                        if (fin < cadena.length() && cadena.charAt(fin) == '.' && isNum(cadena.charAt(fin + 1))) {
                            fin++; //Si hay un punto y es float
                            caso = 3;
                            break;
                        }
                        if(fin<cadena.length() && (isMayus(cadena.charAt(fin)) || isMinus(cadena.charAt(fin)) || cadena.charAt(fin)=='"' )){
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
                            
                          token = "" + genToken.buscaTokenClasif("id_ent");
                        for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                        genToken.guardaToken(tAsign + ",id_ent," + token);

                        inicio = fin;
                        if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            tAsign = "";
                            caso = 0;
                        } else {
                            continuar = false;
                        }
                            
                        }else{ //No cumple con los requerimientos y no debe ser catalagoda con un token.
                            cumple=true;
                            for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                            inicio=fin;
                            
                            genToken.generaError("Error en la palabra: "+tAsign);
                            
                            if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            tAsign = "";
                            caso = 0;
                        } else {
                            continuar = false;
                        }
                        }
                        ;
                        break;

                    case 3: //Genera los numeros con punto flotante.
                        
                        if(fin<cadena.length() && (isMayus(cadena.charAt(fin)) || isMinus(cadena.charAt(fin)))){
                            cumple=false; //Si contiene alguna lentra entonces no aceptar la palabra
                            fin++;
                            break;
                        }
                        
                        if (fin < cadena.length() && isNum(cadena.charAt(fin))) {
                            fin++; //Analizar si es un numero

                        } else { //Si llego al ultimo caso o no es un numero entonces analizar.
                            caso = 4;
                        }
                        ;
                        break;

                    case 4: //Asigna el token a la clasificacion de los flotantes.
                        if(cumple==true){
                            token = "" + genToken.buscaTokenClasif("id_dec");
                        for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                        genToken.guardaToken(tAsign + ",id_dec," + token);
                        inicio = fin;
                        if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            tAsign = "";
                            caso = 0;
                        } else {
                            continuar = false;
                        }
                        
                        }else{ //No cumple con los requerimientos del token
                            for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                            inicio=fin;
                            
                            genToken.generaError("Error en: "+tAsign);
                            
                            if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            tAsign = "";
                            caso = 0;
                            cumple=true;
                        } else {
                            continuar = false;
                        }
                        }
                        ;
                        break;
                    case 5: // Asigna un valor de caracter
                    	if(isCaracterDouble(cadena.charAt(fin-1))&& fin-1!=-1 && banderadoble==false && Confirm_double_3(cadena.charAt(fin)))//compara si hay algo detras de el
                    	{
                    		token = "" + genToken.buscaTokenCar("" + cadena.charAt(fin-1)+cadena.charAt(fin));
                			for (int i = fin-1; i <= fin; i++) {
                                tAsign = "" + tAsign + cadena.charAt(i);
                                System.out.println("doble: "+cadena.charAt(i));
                            }
                			genToken.tiratoken();//borra el ultimo
                            genToken.guardaToken(tAsign + ",Caracter doble," + token);
                            fin++;
                            inicio = fin;
                            banderadoble=true;
                            if (fin < cadena.length()) { //Si la cadena ya llego a su fin, no volver a entrar
                                tAsign = "";
                                caso = 0;
                                break;
                            } else {
                                continuar = false;
                                break;
                            }
                    	}
                    	else {//no tiene nada antes, por lo tanto es uno simple o tiene algo 
                    		token = "" + genToken.buscaTokenCar("" + cadena.charAt(fin));
                            tAsign = "" + cadena.charAt(fin);
                            genToken.guardaToken(tAsign + ",Caracter simple," + token);
                            fin++;
                            inicio = fin;
                            banderadoble=false;
                            if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                                tAsign = "";
                                caso = 0;
                                break;
                            } else {
                                continuar = false;
                               
                            }
                    	}
                        break;
                    
                    case 6:
//                        System.out.println(cadena.charAt(fin));
                        if (fin < cadena.length()) { //Si es menor al tamaÃ±o de la cadena entonces compara
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
                          }else if (bande==2){
                          	token = "" + genToken.buscaTokenClasif("id_clase"); 
                          }else if (bande==3) {
                          	token = "" + genToken.buscaTokenClasif("id_func"); 
                          }

                        for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }
                        genToken.guardaToken(tAsign + "," + token);
                        bande=0;//reinicia la bandera
                        inicio = fin;
                        
                        }else{ //Si no cumple con los requisitos entonces genera un error
                           for (int i = inicio; i < fin; i++) {
                            tAsign = "" + tAsign + cadena.charAt(i);
                        }   
                         inicio=fin;
                         genToken.generaError("Error en: "+tAsign);
                         bande=0;
                        }
                        if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                            cumple=true;
                            tAsign = "";
                            caso = 0;
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
                            genToken.guardaToken(tAsign+",Palabra Reservada,"+token);
                            
                        }else{ //No es una palabra reservada
                            genToken.generaError("Error en: "+tAsign);
                        }
                        inicio=fin; //Mueve el inicio al fin
                        if (fin < cadena.length()) { //Si la cadena ya llegÃ³ a su fin, no volver a entrar
                            tAsign = "";
                            caso = 0;
                        } else {
                            continuar = false;
                        }
                        ;break;
                    case 10: //IDENTEIFICADOR checa si la palabra es mayor o menor
                    	if (fin < cadena.length()) {
                    		if (isMayus(cadena.charAt(fin))) { //Si encuentra una mayuscula para analizarla como Identificador
                                caso = 6;
                                fin++;
                                bande=1;
                                break;
                            }else if (cadena.charAt(fin)==':' || isNum(cadena.charAt(fin)) || isCaracter(cadena.charAt(fin)) ||isMinus(cadena.charAt(fin))) {
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
                                if (isCad(cadena.charAt(fin))) {
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
                            genToken.guardaToken(tAsign + "," + token);

                            inicio = fin;
                            
                            }else{ //Si no cumple con los requisitos entonces genera un error
                               for (int i = inicio; i < fin; i++) {
                                tAsign = "" + tAsign + cadena.charAt(i);
                            }   
                             inicio=fin;
                             genToken.generaError("Error en: "+tAsign);
                            }
                            if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                cumple=true;
                                tAsign = "";
                                caso = 0;
                            } else {
                                continuar = false;
                            }
                            
                            break;
                    case 13: // Asigna un valor de caracter
            
                        token = "" + genToken.buscaTokenCar(""+cadena.charAt(fin-1)+cadena.charAt(fin));
                        tAsign = "" + cadena.charAt(fin-1) + cadena.charAt(fin);
                        genToken.guardaToken(tAsign + ",op_asig," + token);
                        fin++;
                        inicio = fin;

                        if (fin < cadena.length()) { //Si la cadena ya llego a su fin
                            tAsign = "";
                            caso = 0;
                            break;
                        } else {
                            continuar = false;
                        }

                        ;
                        break;
                    case 14:
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
                    	if (fin<cadena.length()) {
                    		if (cumple==true) {
                    			if(cadena.charAt(fin)=='\'') {
                    				fin++;//podemos decir que ya estuvo
                    				token = "" + genToken.buscaTokenClasif("id_cart");
                                    for (int i = inicio; i < fin; i++) {
                                        tAsign = "" + tAsign + cadena.charAt(i);
                                    }
                                    genToken.guardaToken(tAsign + "," + token);
                                    inicio = fin;
                    			}else {
                    				fin++;
                    				cumple=false;
                    				break;
                    			}
                    			if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                    cumple=true;
                                    tAsign = "";
                                    caso = 0;
                                } else {
                                    continuar = false;
                                }
                    		}else {
                    			fin++;
                    			for (int i = inicio; i < fin; i++) {
                                    tAsign = "" + tAsign + cadena.charAt(i);
                                } 
                                       genToken.generaError("Error en: "+tAsign);
                                       inicio = fin;
                                 if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                     cumple=true;
                                    
                                     tAsign = "";
                                     caso = 0;
                                 } else {
                                     continuar = false;
                                 }
                    		}	
                    	}
               	
                    	break;
                    case 16:
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
                                   genToken.generaError("Error en: "+tAsign);
                                   inicio = fin;
                                   tAsign = "";
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
                            genToken.guardaToken(tAsign + "," + token);
                            inicio = fin;
                            continuar = false;
                    	}
                    	break;
                    case 20:
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
                    		caso=22;
                    		for (int i = inicio; i < fin; i++) {
                                tAsigtemp+= ""+cadena.charAt(i);
                            }
                    		tAsigtemp+="\n";
                    		if ((linea+1) == archivo.listLenght()) {//paso el umbral de deteccion
                    			banderacomment=false; //reinicia la bendera
                                inicio=fin;
                                genToken.generaError("Error en: "+tAsigtemp);
                    	}}
                    	break;
                    case 21:
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
                    		caso=22;
                    		for (int i = inicio; i < fin; i++) {
                                tAsigtemp= "" + tAsigtemp + cadena.charAt(i);
                            }
                    		tAsigtemp+="\n";
                    		if ((linea+1) == archivo.listLenght()) {//paso el umbral de deteccion
                    			banderacomment=false; //reinicia la bendera
                                inicio=fin;
                                genToken.generaError("Error en: "+tAsigtemp);
                    	}}
                    	break;
                    case 22: //parecido al caso 7
                    	if (cadena.charAt(fin-1) == '/' && cadena.charAt(fin-2)=='*'){ //por si llego al fi
                            cumple=true; 
                            }
                                
                            if(cumple==true){ //Si cumple los requisitos de ser comentario
                            	token="";
                            	token = "" + genToken.buscaTokenClasif("ini_com"); 
                            for (int i = inicio; i < fin; i++) {
                                tAsigtemp = "" + tAsigtemp + cadena.charAt(i);
                            }
                            genToken.guardaToken(tAsigtemp + "," + token);
                            inicio = fin;
                            banderacomment=false; //reinicia la bendera                            
                            }else{ //Si no cumple con los requisitos entonces genera un error
                             banderacomment=false; //reinicia la bendera
                             inicio=fin;
                             genToken.generaError("Error en: "+tAsigtemp);
                          
                            }
                            if (fin < cadena.length()) { //Si la cadena llego a su fin y no vuelve a entrar
                                cumple=true;
                                tAsign = "";
                                caso = 0;
                            } else {
                                continuar = false;
                            }
                    	break; 
                }

            } while (continuar == true); //Sale del ciclo del automata si se le dijo que ya analizó la linea
            if (banderacomment==true) {
            	//solo es para mantener el caso
            }else {
            	 caso = 0;
            	System.out.println("");
                System.out.println("Analisis de la linea: "+(linea+1));
                System.out.println("");
                System.out.println("Lexema | Clasificacion | Atributo");
                genToken.imprimeTokens();
                System.out.println("");
                System.out.println("Errores generados. ");
                genToken.imprimeError();
                tAsign="";
            }
            
            fin = 0; //Reinicia los valores antes de volver a comprobar una nueva linea dentro del programa
            inicio = 0;
            continuar = true;
           
            bande=0;
        }
        //genToken.imprimeTablas();
    }
    private final Pattern pattern;
    private char c[]= {'<','>',' ','|','@','=','/','*','+','-','.','{','}',',','¡','!','?','¿','#','(',')'};
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

    public boolean isNum(char cad) { //Verifica si es un numero el que se encuentra
        boolean esNum = false;
        if (cad >= '0' && cad <= '9') {
            esNum = true;
        }
        return esNum;
    }
    private char operadores[]= {'>','<','=','!','|',':',';','+','-','*','/','(',')',';','&',','};
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
			"clase","imprime","lectura","retorna","dec","ent","cad","cart"};
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
    /*USANDO UN TRY CATCH PERO A MR PEDEJO NO LE GUSTA
     * 
     * case 21:
                    	if (fin<cadena.length()) {
                    		try {
                    			if (cadena.charAt(fin)=='*' && cadena.charAt(fin+1)=='/') {
                    				caso=21;
                    				fin=fin+2;//recorre2 espacios
                    				break;
                    			}
                    		}catch (NullPointerException e) {//es solo un caracter dentro del comentario
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
                    		for (int i = inicio; i < fin; i++) {
                                tAsigtemp+= "" + tAsign + cadena.charAt(i);
                            }
                    		tAsigtemp+="\n";
                    	}
                    	break;
*/
}