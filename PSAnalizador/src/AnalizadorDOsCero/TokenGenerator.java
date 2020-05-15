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
                                 {"id_func","58"}};  //clasificacion, atributo
  
 private String[][] tablaSimbolos={{";","puntcoma","0032"},  //Lexema, clasificacion, atributo
		 						 {"/","op_div","09"},
		 						 {"->","op_asig","10"},
                                 {"!","op_negacion","11"},
                                 {"+","op_sum","12"},
                                 {"-","op_res","13"},
                                 {"*","op_mult","14"},
                                 {"==","op_comp","15"},
                                 {"=!","op_comp","15"},
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
                                 {"inicio","incio","0028"},
                                 {"final","finale","0029"},
                                 {"principal","principal","0054"},
                                 {"ejecuta","principal","0055"},
                                 {"verdadero","principal","0030"},
                                 {"falso","principal","0030"},
                                 {"si","si","0031"},
                                 {"para","para","0031"},
                                 {"sino","sino","0031"},
                                 {"mientras","mientras","0031"},
                                 {"ala","ala","0031"},
                                 {"mod","mod","0031"},
                                 {"tallo","tallo","0031"},
                                 {"ruptura","ruptura","0031"},
                                 {"cambio","cambio","0031"},
                                 {"caso","caso","0031"},
                                 {"ent","defaulti","0031"},
                                 {"dec","defaulti","0031"},
                                 {"cad","defaulti","0031"},
                                 {"cart","defaulti","0031"},
                                 {"bool","defaulti","0031"},
                                 {"default","defaulti","0031"},
                                 {"crear","crear","0031"},
                                 {"funcion","funcion","0031"},
                                 {"imprime","imprime","0031"},
                                 {"lectura","lectura","0031"},
                                 {"retorna","retorna","0031"}};
 


  
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
 
 public int buscaTokenP(String palabra){
     int token=0;
     for (int i = 0; i < 3; i++) {
         if(tablaReservadas[i][0].equals(palabra)){
             token=Integer.parseInt(tablaReservadas[i][2]);
             break;
         }
     }
     return token;
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
 
 public void imprimeTablas(){
     System.out.println("");
     for (int i = 0; i < tablaToken.length; i++) { //Imprime caracteres
         for (int j = 0; j < 2; j++) {
             System.out.print(tablaToken[i][j]+"|"); 
         }
         System.out.println("");
     }
     
     System.out.println("");
     for (int i = 0; i < tablaSimbolos.length; i++) { //Imprime palabras reservadas
         for (int j = 0; j < 3; j++) {
             System.out.print(tablaSimbolos[i][j]+"|");
         }
         System.out.println("");
     }
     
     System.out.println("");
     for (int i = 0; i < tablaReservadas.length; i++) { //Imprime identificadores, Enteros y Flotantes
         for (int j = 0; j < 3; j++) {
             System.out.print(tablaReservadas[i][j]+"|");
         }
         System.out.println("");
     }
     System.out.println("");
 }
}
