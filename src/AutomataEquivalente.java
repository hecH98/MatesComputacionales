import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class AutomataEquivalente {
	String[][] automata1, automata2;
	String[] lenguaje1, lenguaje2;
	int[] estadoFinal1, estadoFinal2;
	boolean banderaEquivalentes = true;
	int contador = 1;

	public AutomataEquivalente(String[][] automata1, String[][] automata2) {
		if(automata1[0][0].equals("1")) {
			System.out.println("Automata 1 es no determinista\n");
			this.automata1 = obtenerAutomata(AFNtoAFD(automata1));
		}
		else {
			this.automata1 = obtenerAutomata(automata1);
		}

		if(automata2[0][0].equals("1")) {
			System.out.println("Automata 2 es no determinista\n");
			this.automata2 = obtenerAutomata(AFNtoAFD(automata2));
		}
		else {
			this.automata2 = obtenerAutomata(automata2);
		}
		this.lenguaje1 = obtenerLenguaje(automata1);
		this.lenguaje2 = obtenerLenguaje(automata2);
		boolean bandera=false;
		for (int i = 0; i < lenguaje1.length; i++) {
			if(lenguaje1[i].compareTo(lenguaje2[i]) != 0){
				bandera=true;
			}
		}
		if(bandera) {
			System.out.println("Los automatas reciben lenguaje distinto");
		}
		else {
			comparacionMoore(this.automata1, this.automata2);
		}
	}

	private String[][] obtenerAutomata(String[][] automata){
		String[][] automataTmp = new String[automata.length-1][automata[1].length];

		for(int i = 1; i < automata.length; i++) {
			for (int j = 0; j < automata[i].length; j++) {
				automataTmp[i-1][j] = automata[i][j];
			}
		}

		for(int i = 0; i < automataTmp.length; i++) {
			for (int j = 0; j < automataTmp[i].length; j++) {
				System.out.print("[" + automataTmp[i][j] + "]");
			}
			System.out.println();
		}
		System.out.println();
		return automataTmp;
	}

	private void comparacionMoore(String[][] automata,String[][] automata2) {
		String[][] arreglo = new String[automata.length*automata2.length][automata[0].length];

		for (int i = 0; i < arreglo[0].length; i++) {
			arreglo[0][i] = automata[0][i] + "," + automata2[0][i];
			if(arreglo[0][0].compareTo(arreglo[0][i]) != 0) {
				arreglo[contador][0] = arreglo[0][i];
				contador++;
			}
		}

		for (int i = 1; i < arreglo.length; i++) {
			for (int j = 1; j < arreglo[i].length; j++) {
				if(arreglo[i][0]!=null) {
					arreglo[i][j] = agregarCelda(arreglo[i][0], j);
					if(!checarDuplicado(arreglo[i][j], arreglo, i, j)) {
						arreglo[contador][0] = arreglo[i][j];
						contador++;
					}
				}
			}
		}

		for(int i = 0; i < arreglo.length; i++) {
			for (int j = 0; j < arreglo[i].length; j++) {
				if(arreglo[i][0]!= null) {
					System.out.print("[" + arreglo[i][j] + "]");
				}
			}
			if(arreglo[i][0]!= null) {
				System.out.println();
			}

		}

		for(int i = 0; i < arreglo.length; i++) {
			for (int j = 0; j < arreglo[i].length; j++) {
				if(arreglo[i][0]!= null) {
					verificar(arreglo[i][j]);
				}
			}
		}
		System.out.println();
		System.out.println("Los automatas son equivalentes: " + banderaEquivalentes);
	}
	private String agregarCelda(String cadena, int columna) {
		StringTokenizer st = new StringTokenizer(cadena, ",");
		String str1 = st.nextToken();
		String str2 = st.nextToken();
		String strFinal = "";

		for (int i = 0; i < this.automata1.length; i++) {
			if(str1.compareTo(this.automata1[i][0]) == 0) {
				
				strFinal += this.automata1[i][columna] + ",";
			}
		}

		for (int i = 0; i < this.automata2.length; i++) {
			if(str2.compareTo(this.automata2[i][0])==0) {
				strFinal += this.automata2[i][columna];
			}
		}
		return strFinal;
	}

	private String[] obtenerLenguaje(String[][] automata) {
		String [] lenguaje = new String[automata[0].length-1];

		for(int i = 0; i < 1; i++) {
			for(int j = 0; j < automata[i].length-1; j++) {
				lenguaje[j]=automata[0][j+1];
			}
		}
		return lenguaje;
	}

	private boolean checarDuplicado(String cadena, String[][]arreglo, int a, int b) {
		boolean bandera = false;
		for (int i = 0; i < contador; i++) {
			if(arreglo[a][b].compareTo(arreglo[i][0]) == 0) {
				bandera= true;
			}
		}
		return bandera;
	}

	private void verificar(String elemento) {
		StringTokenizer st = new StringTokenizer(elemento, ",");
		String str1 = st.nextToken();
		String str2 = st.nextToken();
		
		if(str1.contains("*") || str2.contains("*") || (str1.equals("0") || str2.equals("0"))) {
			if(str1.equals("0") && str2.equals("0")) {
				return;
			}
			else if(str1.equals("0") || str2.equals("0")){
				this.banderaEquivalentes=false;
				if((str1.contentEquals("0") && str2.contains("*")) || (str2.contentEquals("0") && str1.contains("*")) ) {
					
				}
			}
			else if(!str1.contains("*") || !str2.contains("*")) {
				this.banderaEquivalentes=false;
			}
		}
	}

	
	public static String[][] AFNtoAFD(String[][] AFN) {
		String[][] AFD = new String[AFN.length*2][AFN[0].length];
		for (int i = 0; i < AFD.length; i++) {
			for (int j = 0; j < AFD[i].length; j++) {
				AFD[i][j] = "";
			}
		}
		Queue<String> queue = new LinkedList<>();
		LinkedList<String> finalizado = new LinkedList<>();
		StringTokenizer st;
		String strTemporal;
		int disponible = 1; 
		int tokens;
		for (int i = 0; i < AFD[1].length; i++) {
			AFD[0][i] = AFN[0][i];
		}
		queue.add(AFN[1][0]);

		while(!queue.isEmpty()) {

			AFD[disponible][0]=queue.remove();
			finalizado.add(AFD[disponible][0]);
			st = new StringTokenizer(AFD[disponible][0],"/");
			tokens = st.countTokens();

			for (int i = 0; i < tokens; i++) {
				strTemporal = st.nextToken();
				if(i < tokens-1) {
					for (int j = 0; j < AFN.length; j++) {
						if(strTemporal.equals(AFN[j][0])) {
							for (int k = 1;  k < AFN[0].length; k++) {
								AFD[disponible][k] += AFN[j][k]+"/";
							}
						}
					}
				}
				else {
					for (int j = 0; j < AFN.length; j++) {
						if(strTemporal.equals(AFN[j][0])) {
							for (int k = 1;  k < AFN[0].length; k++) {


								AFD[disponible][k] += AFN[j][k];
								AFD[disponible][k] = eliminarCeros(AFD[disponible][k]);


								if(queue.contains(AFD[disponible][k]) == finalizado.contains(AFD[disponible][k])){
									queue.add(AFD[disponible][k]);
								}
							}
						}
					}
				}
			}
			disponible++;
		}

		int contadorArray=0;
		for (int i = 0; i < AFD.length; i++) {
			if(AFD[i][0]!="") {
				contadorArray++;
			}
		}
		String[][] ultimo = new String[contadorArray][AFD[0].length];
		for (int i = 0; i < contadorArray; i++) {
			for (int j = 0; j < AFD[i].length; j++) {
				ultimo[i][j]=AFD[i][j];
			}
		}
		AFD=ultimo;
		return AFD;
	}

	private static String eliminarCeros(String cadena) {
		StringTokenizer st= new StringTokenizer(cadena,"/"),
				stContador = new StringTokenizer(cadena,"/");
		int tokens=st.countTokens();
		String resultado="";
		int iteraciones = 0;

		for (int i = 0; i < tokens; i++) {
			String tmp=stContador.nextToken();
			if(!tmp.equals("0")) {
				if(!resultado.contains(tmp)) {
					resultado+=tmp;
					iteraciones++;
				}
			}
		}
		resultado="";
		if(tokens == iteraciones || tokens == 1) {
			return cadena;
		}
		else if(iteraciones == 0){
			return "0";
		}
		else {
			for (int i = 0; i < tokens; i++) {
				String tmp = st.nextToken();
				if(!tmp.equals("0")) {
					if(!resultado.contains(tmp)) {
						if(iteraciones != 1) {
							resultado += tmp+"/";
							iteraciones--;
						}
						else{
							resultado += tmp;
						}
					}
				}
			}
			return resultado;
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// Inputs proyecto
		
		// false
		
		String[][] proyecto1= {{"","a","b"},
				{"q0*","q0*","q2"},
				{"q1","q0*","q2"},
				{"q2","q1","q2"}};
		
		String[][] proyecto2= {{"","a","b"},
				{"r0*","r0*","r2"},
				{"r1*","r3","r0*"},
				{"r2","r3","r0*"},
				{"r3","r1*","r3"}};
		
		// true
		
		String[][] proyecto3= {{"","a","b"},
				{"q0","q1*","q2*"},
				{"q1*","q3","q4"},
				{"q2*","q4","q3"},
				{"q3","q5*","q5*"},
				{"q4","q5*","q5*"},
				{"q5*","q5*","q5*"}};
		
		String[][] proyecto4= {{"","a","b"},
				{"r0","r1*","r1*"},
				{"r1*","r2","r2"},
				{"r2","r3*","r3*"},
				{"r3*","r3*","r3*"}};

		// true
		
		String[][] proyecto5= {{"","a","b"},
				{"q0","q1","q2"},
				{"q1","q3*","q4*"},
				{"q2","q4*","q3*"},
				{"q3*","q5*","q5*"},
				{"q4*","q5*","q5*"},
				{"q5*","0","0"},
				{"0","0","0"}};
		
		String[][] proyecto6= {{"","a","b"},
				{"r0","r1","r1"},
				{"r1","r2*","r2*"},
				{"r2*","0","0"},
				{"0","0","0"}};
		
		// true
		
		String[][] proyecto7= {{"1","a","b"},
				{"q0","q1*","0"},
				{"q1*","0","q0"},
				{"0","0","0"}};
		
		String[][] proyecto8= {{"1","a","b"},
				{"r0","r1*","0"},
				{"r1*","0","r2"},
				{"r2","r3*","0"},
				{"r3*","0","r2"},
				{"0","0","0"}};
		
		// true
		
		
		String[][] proyecto9= {{"1","a","b","c","d","e"},
				{"q0","0","q4","q1","q2","q3"},
				{"q1","q5*","0","0","0","0"},
				{"q2","q5*","0","0","0","0"},
				{"q3","q5*","0","0","0","0"},
				{"q4","0","0","q5*","q5*","q5*"},
				{"q5*","0","0","0","0","0"},
				{"0","0","0","0","0","0"}};
		
		String[][] proyecto10= {{"1","a","b","c","d","e"},
				{"q0","0","q3","q1","q1","q1"},
				{"q1","q2*","0","0","0","0"},
				{"q2*","0","0","0","0","0"},
				{"q3","0","0","q2*","q2*","q2*"},
				{"0","0","0","0","0","0"}};
		
		// pruebas
		
		String[][] proyecto11= {{"1","a","b","c","d","e"},
				{"q0","0","q4","q1","q2","q3"},
				{"q1","q5*","0","0","0","0"},
				{"q2","q5*","0","0","0","0"},
				{"q3","q5*","0","0","0","0"},
				{"q4","0","0","q5*","q5*","q5*"},
				{"q5*","0","0","0","0","0"},
				{"0","0","0","0","0","0"}};
		
		String[][] proyecto12= {{"1","a","b","c","d","e"},
				{"q0","0","0","q1","q1","q1"},
				{"q1","q2*","0","0","0","0"},
				{"q2*","0","0","0","0","0"},
				{"q3","0","0","q2*","q2*","q2*"},
				{"0","0","0","0","0","0"}};
		// pruebas 2
		
		
		String[][] proyecto13= {{"1","a","b"},
				{"q0","q1*","0"},
				{"q1*","0","q0"},
				{"0","0","0"}};
		
		String[][] proyecto14= {{"1","a","b"},
				{"r0","r1*","0"},
				{"r1*","0","r2"},
				{"r2","r3*","0"},
				{"r3*","0","0"},
				{"0","0","0"}};
		
		// pruebas 3
		
		String[][] proyecto15= {{"1","a","b"},
				{"q0","q1*","q2"},
				{"q1*","q2","q0"},
				{"q2","q2","q2"}};
		
		String[][] proyecto16= {{"1","a","b"},
				{"r0","r1*","r4"},
				{"r1*","r4","r2"},
				{"r2","r3*","r4"},
				{"r3*","r4","r2"},
				{"r4","r4","r4"}};
		
		new AutomataEquivalente(proyecto5, proyecto6);
		
	//  ---------- Equivalentes --------------
			String[][] automata1 = {{"","a","b"},
					{"a0*","a2*","a1"},
					{"a1","a1","a1"},
					{"a2*","a0*","a1"}};

			String [][] automata2 = {{"","a","b"},
					{"b0*","b0*","b1"},
					{"b1","b1","b1"}};

			// ---------- No Equivalentes ------------
			String [][] automata3 = {{"","a","b"},
					{"q0","q0","q1"},
					{"q1","q0","q2*"},
					{"q2*","q2*","q2*"}};

			String [][] automata4 = {{"","a","b"},
					{"r0*","r0*","r1"},
					{"r1","r0*","r2*"},
					{"r2*","r2*","r2*"}};

			// ---------- Equivalentes ---------------

			String[][] automata5 = {{"","1","2","3"},
					{"x0","x1*","x2","x2"},
					{"x1*","x3*","x4","x1*"},
					{"x2","x5*","x6","x6"},
					{"x3*","x3*","x6","x6"},
					{"x4","x7*","x4","x8"},
					{"x5*","x3*","x6","x6"},
					{"x6","x6","x6","x6"},
					{"x7*","x3*","x4","x6"},
					{"x8","x3*","x4","x6"}};

			String[][] automata6 = {{"","1","2","3"},
					{"y0","y3*","y1","y1"},
					{"y1","y8*","y2","y2"},
					{"y2","y2","y2","y2"},
					{"y3*","y5*","y6","y4*"},
					{"y4*","y5*","y6","y4*"},
					{"y5*","y5*","y2","y2"},
					{"y6","y9*","y6","y7"},
					{"y7","y5*","y6","y2"},
					{"y8*","y5*","y2","y2"},
					{"y9*","y5*","y6","y2"}};

			String[][] automata7 = {{"","1","2","3"},
					{"z0","z1*","z2","z2"},
					{"z1*","z3*","z4","z1*"},
					{"z2","z3*","z5","z5"},
					{"z3*","z3*","z5","z5"},
					{"z4","z6*","z4","z7"},
					{"z5","z5","z5","z5"},
					{"z6*","z3*","z4","z5"},
					{"z7","z3*","z4","z5"}};

			// ---------- No Equivalentes ------------

			String[][] automata8 = {{"","0","1"},
					{"c0*","c0*","c1"},
					{"c1","c0*","c1"}};

			String[][] automata9 = {{"","0","1"},
					{"d0*","d1","d2*"},
					{"d1","d1","d2*"},
					{"d2*","d0*","d2*"}};

			// ---------- Equivalentes ---------------

			String[][] automata10 = {{"","0","1"},
					{"e0*","e0*","e1"},
					{"e1","e0*","e1"}};

			String[][] automata11 = {{"","0","1"},
					{"f0*","f1*","f2"},
					{"f1*","f1*","f2"},
					{"f2","f0*","f2"}};

			// ---------- Equivalentes ---------------

			String[][] automata12 = {{"","a","b"},
					{"g0*","g3","g6"},
					{"g1","g6","g0*"},
					{"g2","g1","g5*"},
					{"g3","g1","g5*"},
					{"g4*","g2","g5*"},
					{"g5*","g4*","g6"},
					{"g6","g6","g6"}};

			String[][] automata13 = {{"1","a","b"},
					{"h0*","h1/h2/h4","0"},
					{"h1","0","h0*"},
					{"h2","h3","0"},
					{"h3","0","h0*"},
					{"h4","0","h5"},
					{"h5","h0*","0"},
					{"0","0","0"}};
			
			String[][] automata14 ={{"1","a","b"},
					{"g0","g1/g4*","g3*"},
					{"g1","g1","g2*"},
					{"g2*","0","0"},
					{"g3*","0","0"},
					{"g4*","0","g4*"},
					{"0","0","0"}};
			
			String[][] automata15= {{"","a","b"},
					{"q0","q1*","q2*"},
					{"q1*","q3","q4*"},
					{"q2*","q7","q7"},
					{"q3","q3","q5*"},
					{"q4*","q7","q6*"},
					{"q5*","q7","q7"},
					{"q6*","q7","q6*"},
					{"q7","q7","q7"}};
			
			String[][] automata16 = {{"1","a","b"},
					{"r1","0","r2/r3*"},
					{"r2","r1/r3*","0"},
					{"r3*","0","0"},
					{"0","0","0"}};
			
			String[][] automata17= {{"","a","b"},
					{"q0","q3","q2*"},
					{"q2*","q1*","q3"},
					{"q1*","q3","q2*"},
					{"q3","q3","q3"}};
			
			// ------------------  equivalentes -------------------------
			String[][] automata18= {{"1","a","b"},
					{"q0*","q1/q2/q4","0"},
					{"q1","0","q0*"},
					{"q2","q3","0"},
					{"q3","0","q0*"},
					{"q4","0","q5"},
					{"q5","q0*","0"},
					{"0","0","0"}};
			
			String[][] automata19= {{"1","a","b"},
					{"r0*","r1","0"},
					{"r1","r2","r0*/r3"},
					{"r2","0","r0*"},
					{"r3","r0*","0"},
					{"0","0","0"}};
			
			String[][] automata20= {{"1","a","b"},
					{"q0","q0/q3","q2*/q1"},
					{"q1","0","q0"},
					{"q2*","q3","q2*"},
					{"q3","q2*","0"},
					{"0","0","0"}};
			
			// pruebas examen parcial
			
			String[][] automata21= {{"1","a","b"},
					{"q0*","q1*/q2","q1*/q2/q3"},
					{"q1*","0","0"},
					{"q2","q2/q1*","q3/q2"},
					{"q3","0","q1*"},
					{"0","0","0"}};
			
			String[][] automata22= {{"","a","b"},
					{"q0*","q2*","q1*"},
					{"q1*","q2*","q3*"},
					{"q2*","q2*","q4"},
					{"q3*","q2*","q3*"},
					{"q4","q2*","q3*"}};
			
			String[][] automata23= {{"","a","b"}, // no borrar ni editar
					{"q0*","q1*","q0*"},
					{"q1*","q1*","q2"},
					{"q2","q1*","q0*"}};
		
		
		
		
	}
}
