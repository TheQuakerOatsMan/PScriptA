package AnalizadorDOsCero;

public class TokenGenerator {
    /*Ascii de simbolos: ; es 59
     = es 61
     + es 43
     - es 45
     * es 42
     ( es 40
     ) es 41
    program es 258
    begin es 259
    end es 260
    identificador es 290
    entero es 291
    flotante es 292
    */
    
  private String[][] tablaToken={ 
                                 {"identificador","1"},
                                 {"id_ent","2"},
                                 {"id_dec","35"},
                                 {"id_cad","36"},
                                 {"id_cart","37"},
                                 {"id_clase","57"},
                                 {"id_func","58"},
                                 {"comment","41"},
                                 {"ini_com","42"},
                                 {"fin_com","43"}};  //clasificacion, atributo
  
 private String[][] tablaSimbolos={{";","puntcoma","0032"},  //Lexema, clasificacion, atributo
		 						 {"/","op_div","09"},
		 						 {"->","op_asig","10"},
                                 {"!","op_negacion","11"},
                                 {"+","op_sum","12"},
                                 {"-","op_res","13"},
                                 {"*","op_mult","14"},
                                 {"==","op_comp","15"},
                                 {"=!","op_compd","60"},
                                 {">","op_may","19"},
                                 {"<","op_min","20"},
                                 {">=","op_mayi","21"},
                                 {"<=","op_mini","22"},
                                 {"&","op_and","38"},
                                 {"!!","op_not","39"},
                                 {"||","op_or","40"},
                                 {"(","abP","33"},
                                 {")","ciP","34"},
                                 {",","del_id","55"},
                                 {"?:","op_cond","52"}};

 private String[][] tablaReservadas={{"clase","clase","0046"},  //Lexema, clasificacion, atributo
                                 {"inicio","inicio","0028"},
                                 {"final","finale","0029"},
                                 {"principal","principal","0054"},
                                 {"ejecuta","ejecuta","0055"},
                                 {"verdadero","verdadero","0030"},
                                 {"falso","falso","0031"},
                                 {"si","si","0003"},
                                 {"para","para","0004"},
                                 {"sino","sino","0005"},
                                 {"mientras","mientras","0007"},
                                 {"hacer","hacer","0008"},
                                 {"ala","ala","0031"},
                                 {"mod","mod","0031"},
                                 {"tallo","tallo","0031"},
                                 {"ruptura","ruptura","0049"},
                                 {"cambio","cambio","0050"},
                                 {"caso","caso","0031"},
                                 {"ent","ent","0023"},
                                 {"dec","dec","0024"},
                                 {"cad","cad","0025"},
                                 {"cart","cart","0026"},
                                 {"bool","bool","0027"},
                                 {"default","defaulti","0053"},
                                 {"crear","crear","0044"},
                                 {"funcion","func","0045"},
                                 {"proc","proc","0061"},
                                 {"imprime","imprime","0057"},
                                 {"lectura","lectura","0047"},
                                 {"retorna","return","0048"},
                                 {"endproc","endproc","0062"},//final de funcion
                                 {"endf","endf","0063"},//final de funcion
                                 {"endprin","endprin","0064"},//final de principal (ESTE COMO UN MAIN)
                                 {"endpara","endpara","0065"},//final del for
                                 {"endif","endif","0066"},//final de if
                                 {"endw","endw","0067"},//final del while
                                 {"endcam","endcam","0068"}};//final del cambio (switch)
 									//el finale lo dejamos para el final de clase


  
 ListaSencilla lTokens=new ListaSencilla();
 ListaSencilla lErrores=new ListaSencilla();
 
 

 
//Busca el token correspondiente si es por Lexema para aquellos unicos
 public int buscaTokenCar(String Lexema){
     int token=0;
     for (int i = 0; i < tablaSimbolos.length; i++) {
           if(tablaSimbolos[i][0].equals(Lexema)){
               token=Integer.parseInt(tablaSimbolos[i][2]); 
                break;
           }     
     }
  return token;
 }
 //busca el token correspondiente por clasificacion para aquellos que no son unicos
 public int buscaTokenClasif(String Clasif){
     int token=0;
     for (int i = 0; i < tablaToken.length; i++) {
           if(tablaToken[i][0].equalsIgnoreCase(Clasif)){
               token=Integer.parseInt(tablaToken[i][1]); 
                break;
           }     
     }
  return token;
 }
 public String buscaTokenPalabra(int c, String cad) {
	 String tokenMot="";
	 if (c==1) {//palabras reservadas
		 for (int i = 0; i < tablaReservadas.length; i++) {
	         if(tablaReservadas[i][0].equals(cad)){
	             tokenMot=tablaReservadas[i][1];
	             break;
	         }
	     }
		 return tokenMot;
	 }
	 if (c==2) {//caracteres simples
	for (int i = 0; i < tablaSimbolos.length; i++) {
        if(tablaSimbolos[i][0].equals(cad)){
            tokenMot=tablaSimbolos[i][1];
            break;
        }
    }
	 return tokenMot; 
	 }
	 if (c==3) {//identificadores y literales
	for (int i = 0; i < tablaToken.length; i++) {
        if(tablaToken[i][1].equals(cad)){
            tokenMot=tablaToken[i][0];
            break;
        }
    }
	 return tokenMot;
}
	 return tokenMot; 
 }
 
 
 public int buscaTokenP(String palabra){
     int token=0;
     for (int i = 0; i < tablaReservadas.length; i++) {
         if(tablaReservadas[i][0].equals(palabra)){
             token=Integer.parseInt(tablaReservadas[i][2]);
             break;
         }
     }
     return token;
 }
 public String tokenlexemanum() {
	 if (!lTokens.isEmpty()) {
		 return lTokens.getValor(lTokens.listLenght()-1);
	 }
	 return "";
 }
 //Guarda el lexema, clasificación y token que hayamos encontrado, y lo guarda en una lista
 
 public void guardaToken(String token){
   lTokens.addValue(token);
 }
 public void tiratoken() {
	lTokens.borrar_ultimo();
 }
 
 public int tokenLenght(){
     int tToken=0;
     tToken=lTokens.listLenght();
     return tToken;
 }
 
 public String tokenValue(int vToken){
      String valor;
          valor=lTokens.getValor(vToken);
      
     return valor;
 }
 
 public void generaError(String error){
    lErrores.addValue(error);
 }
 
 public void imprimeError(){
     for (int i = 0; i < lErrores.listLenght(); i++) {
         System.out.println(lErrores.getValor(i)	);
     }
 }
 public void imprimeTokens(){
     for (int i = 0; i < lTokens.listLenght(); i++) {
         System.out.println( lTokens.getValor(i));
     }
 }
}
