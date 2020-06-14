package AnalizadorDOsCero;

public class pruebas {
	public static void main(String[] args) {
		Alexico e= new Alexico("D:\\Bibliotecas\\Documentos\\otro.pau");
		System.out.println("si jalo?");
		e.genToken.imprimeTokens();
	}
}
