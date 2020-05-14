package AnalizadorDOsCero;

public class simbolos {
	String simbolos [] = {
			"si",
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
			"defaulti",
			"ent",
			"dec",
			"cad",
			"cart",
			"bool",
			"inicio",
			"finale",
			"verdadero",
			"falso",
			"log_not",
		"log_and","log_or","crear","funcion","clase",
		"id_clase","id_func","imprime","lectura","retorna","identificador"
		,"id_dec","id_ent","id_cad","id_cart","op_asig","op_cond","op_negacion"
		,"op_div","op_mult","op_sum","op_res",
		"op_comp","op_may","op_min","op_mayk","op_mink"
		,"puntcoma","abP","ciP","ini_com","fin_com"
		,"del_id","comment"};
	
	public simbolos() {

	}
	String retornaS(int n) {
		String tokens = simbolos[n];
		return tokens;
	}
	String TokenError() {
		return "error";
	}
}
