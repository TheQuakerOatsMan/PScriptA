package AnalizadorDOsCero;


public class ListaSencilla {
  Nodo inicio, fin;
  int cantidad;
    
    public ListaSencilla () {
        inicio = fin = null;
    }
    //limpia xdxdxd
    public void clear() {
		  inicio=fin=null;    
	}

    public boolean isEmpty() {
		if(inicio == null && fin ==null) return true;
		return false;
	}
    
    void addValue(String valor){
        Nodo nuevo = new Nodo (valor); //Se crea un tipo nodo
        if (inicio == null) { //Si está vacia crea uno nuevo
            inicio = fin = nuevo;
        }else{
          fin.der= nuevo; //se amplia
          fin=fin.der;  
        } 
    }

    void imprime() {
        //Recorremos la lista e impriminos todos su contenido
        //Cont recorre la lista elemento x elemento
        Nodo cont = inicio ;
        while (cont != null) {
            System.out.println(cont.nombre);
            //Se recorre la lista una posicion 
            cont = cont.der;
        }
        
    }
    String imprimeR() {
        //Recorremos la lista e impriminos todos su contenido
        //Cont recorre la lista elemento x elemento
        Nodo cont = inicio ;
        String conte="";
        while (cont != null) {
        	conte=cont.nombre;
        	conte+="\n";
            //Se recorre la lista una posicion 
            cont = cont.der;
        }
        return conte;
    }
    
    //Contar los elementos de la lista
    public int listLenght() {
       int elem = 0;
       Nodo cont = inicio ;
        while (cont != null) {
            elem ++;
            //Se recorre la lista una posicion 
            cont = cont.der;
        }
       
       return elem; 
    }

    void borrar_primero() {
        /* Asignar inicio a una variable
            mover el inicio
        desconectar la variable de la lista        
        */
    	if(!isEmpty()) {
			if(inicio==fin) { //Hay un solo elemento
				inicio=fin=null;
			}else {
				inicio=inicio.der;
			}
		}
       /* Nodo v = inicio;
        inicio = inicio.der;
        v.der = null;*/
    }
	public boolean EliminarEspec(int pos) {
		if(!isEmpty()) {
			if(pos == 0) {
				if(inicio==fin) {	//Solo un elemento
					inicio=fin=null;
					return true;
				}else {	//Hay más de un elemento
					inicio=inicio.der;
					return true;
				}
			}else {
				//Localizar la posición
				Nodo ant=null;Nodo temp=inicio;int C=0;
				while(temp!=null && C!=pos) {
					ant=temp;
					temp=temp.der;
					C++;
				}
				
				if(temp == null) {
					System.out.println("No se encuentra la posicón " + pos + " para eliminarlo.");
					return false;
				}else{
					//Sea el ultimo elemento de la lista
					if(temp==fin) {
						ant.der=null;fin=ant;
						return true;
					}else {
						ant.der=temp.der;
						return true;
					}	
				}
					
			}
		}else
			System.out.println("\nLista vacía");
		return false;
	}
	public void setValueAt( int Pos, String p) {
		Nodo obj=new Nodo(p);
		if(isEmpty())
			inicio=fin=obj;
		else {
			
			if(Pos == 0) {
				obj.der=inicio;
				inicio=obj;
			}else{
				//Buscar posición
				Nodo ant=null, rec=inicio;
				int C=0;
				while(rec!=null && C!=Pos) {
					ant=rec;
					rec=rec.der;
					C++;
				}
				
				if(rec!=null) {
					//Si encuentra la posición
					ant.der=obj;
					obj.der=rec;
				}else {
					//No se encuentra
					fin.der=obj;
					fin=obj;
				}
			}
		}
	}
	//retorna la pos del elemento
	public int indexOf(String a) {
		int pos=0;Nodo temp=inicio;
		while(temp != null && !temp.nombre.equalsIgnoreCase(a)) {
			temp=temp.der;
			pos++;
		}
		if(temp==null) {
			return -1; //No se encontró
		}else {
			return pos; //Si lo encontró
		}
	}

    void borrar_ultimo () {
        if (inicio == fin) {
            inicio = fin = null;
        } else {
        Nodo v = inicio;
        while (v.der != fin) {
            v = v.der;
        }
        v.der = null;
        fin = v;
        }
    }

    
    public String getValor(int i) {
        String valor="";
        int c=0;
        //Recorremos la lista e impriminos todos su contenido
        //Cont recorre la lista elemento x elemento
        Nodo cont = inicio ;
        while (cont != null) {
            if(c==i){
                valor=cont.nombre; //Saca el valor
            }
            //Se recorre la lista una posicion
            c++;
            cont = cont.der;
        }
        return valor;
    }
    public boolean contiene(String v) {
    	Nodo temp=inicio;
		while(temp != null && !temp.nombre.equalsIgnoreCase(v)) {
			temp=temp.der;
		}
		if(temp==null) {
			return false; //No se encontró
		}else {
			return true; //Si lo encontró
		}
    }
    
    public class Nodo {
        String nombre;
        Nodo der;
        
        public Nodo (){
            nombre = "";
            der = null;
        }
        public Nodo (String nombre){
            this.nombre = nombre;
            der = null;
        }
    }  
}

