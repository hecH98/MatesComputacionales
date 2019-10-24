import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FNChToFNG {
	LinkedHashMap<String, ArrayList<String>> gramatica, gramaticaOriginal, extras;
	LinkedList<String> terminales, generadores;
	String[][] gramaticaArray;
	int z = 0;
	
	public FNChToFNG(String[][] gramatica) {
		this.terminales = new LinkedList<>();
		this.generadores = new LinkedList<>();
		this.extras = new LinkedHashMap<>();
		this.gramatica = cambiarGeneradores(gramatica);
		System.out.println("Forma Normal de Chomsky");
		printGrammar(this.gramatica);
//		System.out.println("GramaticaOriginal-> " +this.gramaticaOriginal);
//		System.out.println("Gramatica-> " + this.gramatica);
//		System.out.println("Extras-> " + this.extras);
//		System.out.println("Terminales-> " + this.terminales);
//		System.out.println("Generadores-> " + this.generadores);
//		System.out.println();
		conversionFNG();
//		System.out.println();
//		System.out.println("GramaticaOriginal-> " +this.gramaticaOriginal);
//		System.out.println("Gramatica-> " + this.gramatica);
//		System.out.println("Extras-> " + this.extras);
//		System.out.println("Terminales-> " + this.terminales);
//		System.out.println("Generadores-> " + this.generadores);
		this.gramatica = juntarGramaticas();
//		System.out.println(this.gramatica);
		reducirGramatica(this.gramatica);
//		System.out.println(this.gramatica);
//		System.out.println("\nGramatica Final -> "+this.gramatica);
		System.out.println("\nForma Normal de Greibach");
		printGrammar(this.gramatica);
	}
	
	private LinkedHashMap<String, ArrayList<String>> cambiarGeneradores(String[][] gramatica) {
		LinkedHashMap<String, ArrayList<String>> convertido = new LinkedHashMap<>();
		this.gramaticaOriginal = new LinkedHashMap<String, ArrayList<String>>();
		
		for (int i = 0; i < gramatica.length; i++) {
			convertido.put(gramatica[i][0], new ArrayList<String>());
			this.gramaticaOriginal.put(gramatica[i][0], new ArrayList<String>());
			this.generadores.add(gramatica[i][0]);
			for(int j = 1; j < gramatica[i].length; j++) {
				convertido.get(gramatica[i][0]).add(gramatica[i][j]);
				this.gramaticaOriginal.get(gramatica[i][0]).add(gramatica[i][j]);
				if(gramatica[i][j].length() == 1) {
					if(!this.terminales.contains(gramatica[i][j])) {
						this.terminales.add(gramatica[i][j]);
					}
				}
			}
		}
		return convertido;
	}
	
	private void conversionFNG() {
		for(String key : this.gramatica.keySet()) {
//			System.out.println("key: "+key+" size: "+this.gramatica.get(key).size());
			int length = this.gramatica.get(key).size();
			for (int j = 0; j < length; j++) {
//				System.out.println("actual length "+gramatica.get(key).size());
				if(this.gramatica.get(key).get(j).length() != 1) {
//					System.out.println(key);
//					System.out.println(this.gramatica.get(key).get(j));
//					boolean comprobacion = comprobacion(key, Character.toString(this.gramatica.get(key).get(j).charAt(0)));
					while(comprobacion(key, Character.toString(this.gramatica.get(key).get(j).charAt(0)))) {
//						System.out.println("cadena: "+this.gramatica.get(key).get(j));
						String cambiado = this.gramatica.get(key).get(j).substring(0, 1);
						String temporal = this.gramatica.get(key).get(j).substring(1, this.gramatica.get(key).get(j).length());
//						System.out.println("cambiado: "+cambiado);
//						System.out.println("temporal: "+temporal);
						sustitucion(cambiado, temporal, this.gramatica.get(key), key, j);
//						System.out.println("new length "+gramatica.get(key).size());
						length = this.gramatica.get(key).size();
//						System.out.println("new length "+length);
//						System.out.println(this.gramatica);
						
//						comprobacion = comprobacion(key, Character.toString(this.gramatica.get(key).get(j).charAt(0)));
					}
					length = this.gramatica.get(key).size();
				}
			}
			if(checarRecursion(key)) {
				recursionIzquierda(key);
			}
		}
		
		for(String key : this.gramatica.keySet()) {
			for (int j = 0; j < this.gramatica.get(key).size(); j++) {
				if(!this.terminales.contains(Character.toString(this.gramatica.get(key).get(j).charAt(0)))) {
					String cambiado = this.gramatica.get(key).get(j).substring(0, 1);
					String temporal = this.gramatica.get(key).get(j).substring(1, this.gramatica.get(key).get(j).length());
					
					sustitucion(cambiado, temporal, this.gramatica.get(key), key, j);
				}
			}
		}
		
		for(String key : this.extras.keySet()) {
//			System.out.println("key "+key);
			for (int j = 0; j < this.extras.get(key).size(); j++) {
//				System.out.println(extras.get(key).get(j));
				if(!this.terminales.contains(Character.toString(this.extras.get(key).get(j).charAt(0)))) {
					String cambiado = this.extras.get(key).get(j).substring(0, 1);
//					System.out.println(cambiado);
					String temporal = this.extras.get(key).get(j).substring(1, this.extras.get(key).get(j).length());
//					System.out.println(temporal);
					sustitucionZ(cambiado, temporal, this.extras.get(key), key, j);
				}
			}
		}
	}
	
	private void sustitucion(String aCambiar, String resto, ArrayList<String> padre, String key, int columna) {
//		System.out.println("padre "+padre);
//		System.out.println("columna "+columna);
//		System.out.println("aCambiar "+aCambiar);
//		System.out.println("resto "+resto);
//		System.out.println("key "+key);
		String res = new String();
		ArrayList<String> listaTemp = new ArrayList<>();
		
		
//		System.out.println("gramatica "+gramatica.get(key));
		for (int i = 0; i < this.gramatica.get(key).size(); i++) {
			if(i == columna) {
				for (int j = 0; j < this.gramatica.get(aCambiar).size(); j++) {
					res = this.gramatica.get(aCambiar).get(j) + resto;
					listaTemp.add(res);
				}
			}
			else {
				listaTemp.add(padre.get(i));
			}
		}
		this.gramatica.replace(key, listaTemp);
//		System.out.println(listaTemp);
//		System.out.println("res "+res);
	}
	
	private void sustitucionZ(String aCambiar, String resto, ArrayList<String> padre, String key, int columna) {
//		System.out.println("padre "+padre);
//		System.out.println("columna "+columna);
//		System.out.println("aCambiar "+aCambiar);
//		System.out.println("resto "+resto);
//		System.out.println("key "+key);
		String res = new String();
		ArrayList<String> listaTemp = new ArrayList<>();
		
		
//		System.out.println("gramatica "+gramatica.get(key));
		for (int i = 0; i < this.extras.get(key).size(); i++) {
			if(i == columna) {
				for (int j = 0; j < this.gramatica.get(aCambiar).size(); j++) {
					res = this.gramatica.get(aCambiar).get(j) + resto;
					listaTemp.add(res);
				}
			}
			else {
				listaTemp.add(padre.get(i));
			}
		}
		this.extras.replace(key, listaTemp);
//		System.out.println(listaTemp);
//		System.out.println("res "+res);
	}
	
	private boolean checarRecursion(String key) {
		for(int i = 0; i < this.gramatica.get(key).size(); i++) {
			if(this.generadores.indexOf(key) == this.generadores.indexOf(Character.toString(this.gramatica.get(key).get(i).charAt(0)))) {
				return true;
			}
		}
		return false;
	}
	
	private void recursionIzquierda(String key) {
		int[] alpha = getAlpha(key);
		int[] beta = getBeta(key);
//		System.out.println("alphas");
//		for (int i = 0; i < alpha.length; i++) {
//			System.out.print(alpha[i]+",");
//		}
//		
//		System.out.println("\nbetas");
//		for (int i = 0; i < beta.length; i++) {
//			System.out.print(beta[i]+",");
//		}
//		System.out.println();
		ArrayList<String> betas = new ArrayList<String>();
		ArrayList<String> alphas = new ArrayList<String>();
		
		
		for (int i = 0; i < alpha.length; i++) {
			alphas.add(this.gramatica.get(key).get(alpha[i]).substring(1, this.gramatica.get(key).get(alpha[i]).length()));
			alphas.add(this.gramatica.get(key).get(alpha[i]).substring(1, this.gramatica.get(key).get(alpha[i]).length())+"z"+z);
		}
		for (int i = 0; i < beta.length; i++) {
			betas.add(this.gramatica.get(key).get(beta[i]));
			betas.add(this.gramatica.get(key).get(beta[i])+"z"+z);
		}
		this.extras.put("z"+z, alphas);
		
		this.gramatica.replace(key, betas);
		this.z++;
		
//		System.out.println("new alphas "+alphas);
//		System.out.println("new betas "+betas);
	}
	
	private int[] getAlpha(String key){
		int[] array = new int[this.gramatica.get(key).size()];
		int cont=0;
//		System.out.println("key "+key);
		for (int i = 0; i < this.gramatica.get(key).size(); i++) {
//			System.out.println(Character.toString(gramatica.get(key).get(i).charAt(0)));
//			System.out.println(i);
//			System.out.println("cont "+cont);
			if(Character.toString(this.gramatica.get(key).get(i).charAt(0)).contentEquals(key)) {
				array[cont] = i;
				cont++;
//				System.out.println(cont);
			}
		}
		int[] fin = new int[cont];
		for (int i = 0; i < fin.length; i++) {
			fin[i]=array[i];
		}
		return fin;
	}
	
	private int[] getBeta(String key){
		int[] array = new int[this.gramatica.get(key).size()];
		int cont=0;
		for (int i = 0; i < this.gramatica.get(key).size(); i++) {
			if(!Character.toString(this.gramatica.get(key).get(i).charAt(0)).contentEquals(key)) {
				array[cont] = i;
				cont++;
			}
		}
		
		int[] fin = new int[cont];
		for (int i = 0; i < fin.length; i++) {
			fin[i]=array[i];
		}
		return fin;
	}
	
	private boolean comprobacion(String generador, String produccion) {
//		System.out.println(generador+","+produccion);
		if(this.terminales.contains(produccion)) {
			return false;
		}
		if(this.generadores.indexOf(generador) <= generadores.indexOf(produccion)) {
			return false;
		}
		return true;
	}
	
	private LinkedHashMap<String, ArrayList<String>>juntarGramaticas() {
		LinkedHashMap<String, ArrayList<String>> nuevo = new LinkedHashMap<String, ArrayList<String>>();
		for(String key : this.gramatica.keySet()) {
			nuevo.put(key, this.gramatica.get(key));
		}
		for(String key2 : this.extras.keySet()) {
			nuevo.put(key2, this.extras.get(key2));
		}
		
		return nuevo;
	}
	
	private void reducirGramatica(LinkedHashMap<String, ArrayList<String>> grammar) {
		ArrayList<String> cadenas;
		for(String key : grammar.keySet()) {
			cadenas = new ArrayList<String>();
			for (int j = 0; j < grammar.get(key).size(); j++) {
				if(!cadenas.contains(grammar.get(key).get(j))){
					cadenas.add(grammar.get(key).get(j));
				}
			}
			grammar.replace(key, cadenas);
		}
		quitarTerminalesInutiles(grammar);
	}
	
	private void quitarTerminalesInutiles(LinkedHashMap<String, ArrayList<String>> grammar) {
		Queue<String> checar = new LinkedList<String>();
		ArrayList<String> usados = new ArrayList<String>();
		checar.add("S");
		
		while(!checar.isEmpty()) {
			String key = checar.remove();
			usados.add(key);
			for (int j = 0; j < grammar.get(key).size(); j++) {
				
				if(grammar.get(key).get(j).length() != 1) {
					
					for (int i = 1; i < grammar.get(key).get(j).length(); i++) {
						String agregar = grammar.get(key).get(j).substring(i, i+1);
						if(agregar.equals("z")) {
							agregar = grammar.get(key).get(j).substring(i, i+2);
							i++;
						}
						if(!checar.contains(agregar) && !usados.contains(agregar)) {
							checar.add(agregar);
						}
						
					}
//					System.out.println(grammar.get(key).get(j));
				}
			}
		}
//		System.out.println("usados "+usados);
		LinkedHashMap<String, ArrayList<String>> grammat = new LinkedHashMap<String, ArrayList<String>>();
		for (int i = 0; i < usados.size(); i++) {
			grammat.put(usados.get(i), grammar.get(usados.get(i)));
		}
//		System.out.println(grammat);
		this.gramatica = grammat;
	}
	
	private void printGrammar(LinkedHashMap<String, ArrayList<String>> grammar) {
		for (String key : grammar.keySet()) {
			System.out.print(key+" -> ");
			for (int j = 0; j < grammar.get(key).size(); j++) {
				if(j == 0) {
					System.out.print(grammar.get(key).get(j));
				}
				else {
					System.out.print(" | "+grammar.get(key).get(j));
				}
				
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		String[][] gramatica = {{"S", "AE", "AD", "AB", "AC"}, 
        		{"A","1"},
        		{"B","0"},
        		{"C","BB"},
        		{"D","SC"},
        		{"E","SB"}};
		
		String[][] gramatica2 = {{"S", "SC", "AB", "SA", "a"}, 
        		{"A","a"},
        		{"B","b","BB"},
        		{"C","AB"}};
		
		String[][] gramatica3 = {{"S", "AC", "BD", "AA", "BB"}, 
        		{"A","0"},
        		{"B","1"},
        		{"C","XA"},
        		{"D","XB"},
        		{"X","AX","BX","0","1"},};
		
		String[][] gramatica4 = {{"S", "AC", "BD", "AA", "BB"}, 
        		{"A","0"},
        		{"B","1"},
        		{"X","AX","BX","0","1"},
        		{"C","XA"},
        		{"D","XB"}};
		
		new FNChToFNG(gramatica2);
	}

}
