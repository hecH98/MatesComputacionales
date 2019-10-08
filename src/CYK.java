import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class CYK {
	private String[][] tablaCyk;
	private HashMap<String,ArrayList<String>> gramatica = new HashMap<>();
	private String palabra;
	private Nodo root = new Nodo("S");

	public CYK(String[][] gramatica, String palabra) {
		this.palabra = palabra;
		obtenerGramatica(gramatica);
		System.out.println("Gramatica "+this.gramatica);
		crearTabla();
		hacerCyk();
		printTabla(tablaCyk);
	}
	
	public void obtenerGramatica(String[][] gramatica) {
		for (int i = 0; i < gramatica.length; i++) {
       	 this.gramatica.put(gramatica[i][0], new ArrayList<String>());
			for (int j = 1; j < gramatica[i].length; j++) {
				this.gramatica.get(gramatica[i][0]).add(gramatica[i][j]);
			}
		}
	}
	
	public void crearTabla() {
		// se crean las filas de la tabla
		tablaCyk = new String[palabra.length() + 1][];
		
		//se crea la fila donde ira la palabra a checar
		tablaCyk[0] = new String[palabra.length()];
		
		// se van creando las filas, restando una columna por cada fila que se baja
        for(int i = 1; i < tablaCyk.length; i++){
        	tablaCyk[i] = new String[palabra.length() - i + 1];
        }
        
        // cambiar null por "" en cada celda de la tabla
        for(int i = 0; i < tablaCyk.length; i++){
            for(int j = 0; j < tablaCyk[i].length; j++){
            	tablaCyk[i][j] = "";
            }
        }
	}
	
	public void hacerCyk() {
		// llenar la primera fila con la palabra
		for (int i = 0; i < tablaCyk[0].length; i++) {
			tablaCyk[0][i] = Character.toString(palabra.charAt(i));
		}
		
		// llenar la primera fila de simbolos terminales
		for (int i = 0; i < tablaCyk[1].length; i++) {
			tablaCyk[1][i] = buscarProducciones(this.tablaCyk[0][i]);
			if(tablaCyk[1][i].contains(","))
			tablaCyk[1][i] = quitarDuplicados(tablaCyk[1][i]);
		}
		if(palabra.length() < 2) {
			return;
		}
		
		// llenar la segunda fila de simbolos terminales
		for (int i = 0; i < tablaCyk[2].length; i++) {
			String combinaciones = productoCruz(this.tablaCyk[1][i],this.tablaCyk[1][i+1]);
			tablaCyk[2][i] = buscarProducciones(combinaciones);
			if(tablaCyk[2][i].contains(","))
			tablaCyk[2][i] = quitarDuplicados(tablaCyk[2][i]);
		}
		if(palabra.length() < 3) {
			return;
		}
		
		// llenar la tabla para n filas
		for (int i = 3; i < tablaCyk.length; i++) {
			for (int j = 0; j < tablaCyk[i].length; j++) {
//				System.out.println("i: "+i+", j: "+j);
				for (int k = 1; k < i; k++) {
//					System.out.println("control k: "+k);
//					System.out.println("metiendo a producto cruz: "+this.tablaCyk[k][j]);
//					System.out.println("metiendo a producto cruz: "+this.tablaCyk[i-k][j+k]);
					String combinaciones = productoCruz(this.tablaCyk[k][j],this.tablaCyk[i-k][j+k]);
					if(tablaCyk[i][j].equals("")) {
						if(!buscarProducciones(combinaciones).equals(""))
						tablaCyk[i][j] += buscarProducciones(combinaciones);
					}
					else {
						if(!buscarProducciones(combinaciones).equals(""))
						tablaCyk[i][j] += ","+buscarProducciones(combinaciones);
					}
//					if(!this.tablaCyk[i][j].equals("")){
//						break;
//					}
//					System.out.println(this.tablaCyk[k][j]+this.tablaCyk[i-k][j+k]);
//					System.out.println("equals: "+tablaCyk[i][j]);
				}
				if(tablaCyk[i][j].contains(","))
				tablaCyk[i][j] = quitarDuplicados(tablaCyk[i][j]);
//				System.out.println("final: "+tablaCyk[i][j]);
			}
		}
	}
	
	private String buscarProducciones(String palabra) {
		StringTokenizer st = new StringTokenizer(palabra,",");
		String resultado = new String();
		while(st.hasMoreElements()) {
			String word = st.nextToken();
//			System.out.println(word);
			for (String llave : gramatica.keySet()) {
				if(resultado.isEmpty()) {
					if(gramatica.get(llave).contains(word)) {
						resultado += llave;
					}
				}else {
					if(gramatica.get(llave).contains(word)) {
						resultado += ","+llave;
					}
				}
				
			}
		}
//		System.out.println(resultado);
		return resultado;
	}
	
	private static String productoCruz(String a, String b) {
		if(a.isEmpty() || b.isEmpty()) {
			return new String();
		}
//		System.out.println(a);
//		System.out.println(b);
		String resultado = new String();
		StringTokenizer stA = new StringTokenizer(a,",");
		StringTokenizer stB = new StringTokenizer(b,",");
		int tokensA = stA.countTokens();
		for(int i=0; i < tokensA; i++) {
			String palabra1 = stA.nextToken();
//			System.out.println("tokens a: "+stA.countTokens());
			int tokensB = stB.countTokens();
			for(int j=0; j < tokensB; j++) {
//				System.out.println("tokens b: "+stB.countTokens()+", j: "+j);
				String palabra2 = stB.nextToken();
				if(resultado.isEmpty()) {
					resultado += palabra1+palabra2;
				}else {
					resultado += ","+palabra1+palabra2;
				}
//				System.out.println("resultado: "+resultado);
			}
			stB = new StringTokenizer(b,",");
		}
		return resultado;
	}
	
	public String quitarDuplicados(String s) {
		String resultado = new String();
//		System.out.println("entro");
//		System.out.println(resultado);
//		System.out.println(s);
		Queue<String> q = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(s,",");
		
		while(st.hasMoreElements()) {
			String token = st.nextToken();
			if(!q.contains(token)){
				q.add(token);
			}
		}
//		System.out.println(q);
		while(!q.isEmpty()) {
			if(resultado.isEmpty()) {
				resultado += q.remove();
			}else {
				resultado += ","+q.remove();
			}
		}
//		System.out.println(resultado);
		return resultado;
	}
	
	private void printTabla(String[][] tabla) {
		for(int i = 0; i < tabla.length; i++) {
			for(int j = 0; j < tabla[i].length; j++) {
				if(tabla[i][j].isEmpty()) {
					System.out.print("0\t ");
				}
				else {
					System.out.print(tabla[i][j] + "\t ");
				}
			}
			System.out.println();
		}
		System.out.println();
		if(tabla[tabla.length-1][0].contains("S")) {
			System.out.println("La palabra \"" + palabra + "\" se puede formar con la gramatica");
			backTracking("S", tabla.length-1, 0, this.root);
			System.out.println("\n-------Imprimiendo Arbol Amplitud (Arriba-Abajo/Izquierda-Derecha)-------------\n");
			imprimirArbol();
			System.out.println("\n------- Visualizando Arbol (Girado 90 Grados) -------------\n");
			visualizarArbol(this.root,0);
		}
		else {
			System.out.println("La palabra \"" + palabra + "\" no se puede formar con la gramatica");
		}
		
	}
	
	public String backTracking(String llave, int fila, int columna, Nodo curr) {
//		System.out.println(llave);
//		System.out.println(fila);
//		System.out.println(columna);
		int k = fila;
//		System.out.println("arbol");
//		imprimirArbol();
		if(fila==1) {
			curr.addLeft(tablaCyk[0][columna]);
//			System.out.println(tablaCyk[0][columna]);
			return tablaCyk[0][columna];
		}
		else {
			for (int i = 1; i < fila; i++) {
				String combinaciones = productoCruz(tablaCyk[i][columna],tablaCyk[k-i][i+columna]);
//				System.out.println(combinaciones);
				StringTokenizer st = new StringTokenizer(combinaciones,",");
				int tokens = st.countTokens();
				for (int j = 0; j < tokens; j++) {
					String actual = st.nextToken();
					if(gramatica.get(llave).contains(actual)) {
//						System.out.println(actual);
//						System.out.println(actual);
//						System.out.println(Character.toString(s.charAt(0)));
						curr.addLeft(Character.toString(actual.charAt(0)));
						curr.addRight(Character.toString(actual.charAt(1)));
						backTracking(Character.toString(actual.charAt(0)), i, columna, curr.left);
						backTracking(Character.toString(actual.charAt(1)), k-i, i+columna, curr.right);
						return actual;
					}
				}
				
			}
		}
		System.out.println();
		
		return new String();
	}
	
	private void visualizarArbol(Nodo curr, int cont) {
		if(curr != null) {
			visualizarArbol(curr.right, cont+1);
			for (int i = 0; i < cont; i++) {
				System.out.print(", ");
			}
			System.out.println(curr.toString());
			visualizarArbol(curr.left, cont+1);
		}
		
	}
	
	public void imprimirArbol() {
		Queue<Nodo> queue=new LinkedList<>();
		Nodo curr=this.root;
		
		queue.add(curr);
		while(!queue.isEmpty()){
			if(curr.left!=null) {
				queue.add(curr.left);
			}
			if(curr.right!=null) {
				queue.add(curr.right);
			}
			System.out.print(queue.remove()+",");
			
			if(!queue.isEmpty()) {
				curr=queue.peek();
			}
		}
		System.out.println();
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String[][] gramatica = {{"S","AB","SS","AC","BD","BA"},
							  {"A","a"},
							  {"B","b"},
							  {"C","SB"},
							  {"D","SA"}};
		String palabra = "aabbab";
		
		String[][] gramatica2 = {{"S","AB","BC"},
				        		{"A","AB","a"},
				        		{"B","CC","b"},
				        		{"C","AB","a"}};
        String palabra2 = "bab";
        
        String[][] gramatica3 = {{"S","AB","XB"},
        		{"A","a"},
        		{"B","b"},
        		{"X","AS"},
        		{"T","AB","XB"}};
        String palabra3 = "aaabbb";
        
        String[][] gramatica4 = {{"S", "AB"}, 
        		{"C","a"},
        		{"A","CD","CF"},
        		{"B","EB","c"},
        		{"D","b"},
        		{"E","c"},
        		{"F","AD"}};
        
        String palabra4 = "aaabbbcc";
        
		new CYK(gramatica,palabra);
		
	}
}

class Nodo{
	String dato;
	Nodo right, 
		 left;
	
	public Nodo(String dato, Nodo right, Nodo left) {
		this.dato = dato;
		this.right = right;
		this.left = left;
	}

	public Nodo(String dato) {
		this(dato, null,null);
	}
	
	public void addRight(String dato) {
		this.right=new Nodo(dato);
	}
	
	public void addLeft(String dato) {
		this.left=new Nodo(dato);
	}
	
	public String toString() {
		return this.dato;
	}
}
