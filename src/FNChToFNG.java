import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FNChToFNG {
	private LinkedHashMap<String, ArrayList<String>> grammar, extras;
	private LinkedList<String> terminales, generadores;
	private int z = 0;

	public FNChToFNG(String[][] gramatica) {
		this.terminales = new LinkedList<>();
		this.generadores = new LinkedList<>();
		this.extras = new LinkedHashMap<>();
		this.grammar = obtenerGramatica(gramatica);
		System.out.println("Forma Normal de Chomsky");
		printGrammar(this.grammar);
		conversionFNG();
		this.grammar = juntarGramaticas();
		reducirGramatica(this.grammar);
		System.out.println("\nForma Normal de Greibach");
		printGrammar(this.grammar);
	}

	private LinkedHashMap<String, ArrayList<String>> obtenerGramatica(String[][] gramatica) {
		LinkedHashMap<String, ArrayList<String>> convertido = new LinkedHashMap<>();
		for (int i = 0; i < gramatica.length; i++) {
			convertido.put(gramatica[i][0], new ArrayList<String>());
			this.generadores.add(gramatica[i][0]);
			for(int j = 1; j < gramatica[i].length; j++) {
				convertido.get(gramatica[i][0]).add(gramatica[i][j]);
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
		for(String key : this.grammar.keySet()) {
			int length = this.grammar.get(key).size();
			for (int j = 0; j < length; j++) {
				if(this.grammar.get(key).get(j).length() != 1) {
					while(comprobacion(key, Character.toString(this.grammar.get(key).get(j).charAt(0)))) {
						String cambiado = this.grammar.get(key).get(j).substring(0, 1);
						String temporal = this.grammar.get(key).get(j).substring(1, this.grammar.get(key).get(j).length());
						sustitucion(cambiado, temporal, this.grammar.get(key), key, j);
					}
					length = this.grammar.get(key).size();
				}
			}
			if(checarRecursion(key)) {
				recursionIzquierda(key);
			}
		}

		for (int i = generadores.size()-1; i >= 0 ; i--) {
			String key = generadores.get(i);
			for (int j = 0; j < this.grammar.get(key).size(); j++) {
				if(!this.terminales.contains(Character.toString(this.grammar.get(key).get(j).charAt(0)))) {
					String cambiado = this.grammar.get(key).get(j).substring(0, 1);
					String temporal = this.grammar.get(key).get(j).substring(1, this.grammar.get(key).get(j).length());
					sustitucion(cambiado, temporal, this.grammar.get(key), key, j);
				}
			}
		}

		for(String key : this.extras.keySet()) {
			for (int j = 0; j < this.extras.get(key).size(); j++) {
				if(!this.terminales.contains(Character.toString(this.extras.get(key).get(j).charAt(0)))) {
					String cambiado = this.extras.get(key).get(j).substring(0, 1);
					String temporal = this.extras.get(key).get(j).substring(1, this.extras.get(key).get(j).length());
					sustitucionZ(cambiado, temporal, this.extras.get(key), key, j);
				}
			}
		}
	}

	private void sustitucion(String aCambiar, String resto, ArrayList<String> padre, String key, int columna) {
		String res = new String();
		ArrayList<String> listaTemp = new ArrayList<>();
		for (int i = 0; i < this.grammar.get(key).size(); i++) {
			if(i == columna) {
				for (int j = 0; j < this.grammar.get(aCambiar).size(); j++) {
					res = this.grammar.get(aCambiar).get(j) + resto;
					listaTemp.add(res);
				}
			}
			else {
				listaTemp.add(padre.get(i));
			}
		}
		this.grammar.replace(key, listaTemp);
	}

	private void sustitucionZ(String aCambiar, String resto, ArrayList<String> padre, String key, int columna) {
		String res = new String();
		ArrayList<String> listaTemp = new ArrayList<>();
		for (int i = 0; i < this.extras.get(key).size(); i++) {
			if(i == columna) {
				for (int j = 0; j < this.grammar.get(aCambiar).size(); j++) {
					res = this.grammar.get(aCambiar).get(j) + resto;
					listaTemp.add(res);
				}
			}
			else {
				listaTemp.add(padre.get(i));
			}
		}
		this.extras.replace(key, listaTemp);
	}

	private boolean checarRecursion(String key) {
		for(int i = 0; i < this.grammar.get(key).size(); i++) {
			if(this.generadores.indexOf(key) == this.generadores.indexOf(Character.toString(this.grammar.get(key).get(i).charAt(0)))) {
				return true;
			}
		}
		return false;
	}

	private void recursionIzquierda(String key) {
		int[] alpha = getAlpha(key);
		int[] beta = getBeta(key);
		ArrayList<String> betas = new ArrayList<String>();
		ArrayList<String> alphas = new ArrayList<String>();
		for (int i = 0; i < alpha.length; i++) {
			alphas.add(this.grammar.get(key).get(alpha[i]).substring(1, this.grammar.get(key).get(alpha[i]).length()));
			alphas.add(this.grammar.get(key).get(alpha[i]).substring(1, this.grammar.get(key).get(alpha[i]).length())+"z"+z);
		}
		for (int i = 0; i < beta.length; i++) {
			betas.add(this.grammar.get(key).get(beta[i]));
			betas.add(this.grammar.get(key).get(beta[i])+"z"+z);
		}
		this.extras.put("z"+z, alphas);
		this.grammar.replace(key, betas);
		this.z++;
	}

	private int[] getAlpha(String key){
		int[] array = new int[this.grammar.get(key).size()];
		int cont=0;
		for (int i = 0; i < this.grammar.get(key).size(); i++) {
			if(Character.toString(this.grammar.get(key).get(i).charAt(0)).contentEquals(key)) {
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

	private int[] getBeta(String key){
		int[] array = new int[this.grammar.get(key).size()];
		int cont=0;
		for (int i = 0; i < this.grammar.get(key).size(); i++) {
			if(!Character.toString(this.grammar.get(key).get(i).charAt(0)).contentEquals(key)) {
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
		for(String key : this.grammar.keySet()) {
			nuevo.put(key, this.grammar.get(key));
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
				}
			}
		}
		LinkedHashMap<String, ArrayList<String>> grammat = new LinkedHashMap<String, ArrayList<String>>();
		for (int i = 0; i < usados.size(); i++) {
			grammat.put(usados.get(i), grammar.get(usados.get(i)));
		}
		this.grammar = grammat;
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


	@SuppressWarnings("unused")
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
				{"X","AX","BX","0","1"}};
		
		String[][] gramatica4 = {{"S", "AB"}, 
				{"A","BS","b"},
				{"B","SA","a"}};

		new FNChToFNG(gramatica4);
	}

}
