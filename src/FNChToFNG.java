import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class FNChToFNG {
	LinkedHashMap<String, ArrayList<String>> gramatica;
	LinkedHashMap<String, ArrayList<String>> extras;
	LinkedList<String> terminales;
	String[][] gramaticaArray;
	int z = 0;
	
	public FNChToFNG(String[][] gramatica) {
		this.terminales = new LinkedList<>();
		this.extras = new LinkedHashMap<>();
		this.gramatica = cambiarGeneradores(gramatica);
		this.gramaticaArray = gramatica;
		System.out.println("Gramatica-> " + this.gramatica);
		System.out.println("Extras-> " + this.extras);
		System.out.println("Terminales-> " + this.terminales);
		System.out.println(this.gramatica);
	}
	
	private LinkedHashMap<String, ArrayList<String>> cambiarGeneradores(String[][] gramatica) {
		LinkedHashMap<String, ArrayList<String>> convertido = new LinkedHashMap<>();
		
		for (int i = 0; i < gramatica.length; i++) {
			convertido.put(gramatica[i][0], new ArrayList<String>());
			for(int j = 1; j < gramatica[i].length; j++) {
				convertido.get(gramatica[i][0]).add(gramatica[i][j]);
				if(gramatica[i][j].length() == 1) {
					if(!terminales.contains(gramatica[i][j])) {
						terminales.add(gramatica[i][j]);
					}
				}
			}
		}
		return convertido;
	}
	
	private void conversionFNG() {
		for (int i = 0; i < this.gramaticaArray.length; i++) {
			for (int j = 1; j < this.gramaticaArray[i].length; j++) {
				while(comprobacion(this.gramaticaArray[i][0], this.gramaticaArray[i][1])) {
					sustitucion();
				}
				
			}
		}
	}
	
	private void sustitucion() {
		
	}
	
	private void recursionIzquierda() {
		
	}
	
	private boolean comprobacion(String generador, String produccion) {
		return true;
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		String[][] gramatica = {{"S", "AE", "AD", "AB", "AC"}, 
        		{"A","1"},
        		{"B","0"},
        		{"C","BB"},
        		{"D","SC"},
        		{"E","SB"}};
		
		new FNChToFNG(gramatica);
	}

}
