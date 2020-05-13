package AnalizadorDOsCero;

public class nodo {
	 String nombre;
     nodo der;
     
     public nodo (){
         nombre = "Sin nombre";
         der = null;
     }
     public nodo (String nombre){
         this.nombre = nombre;
         der = null;
     }
}
