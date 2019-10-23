import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class FNChToFNG {
	LinkedHashMap<String, ArrayList<String>> gramatica;
	LinkedHashMap<String, ArrayList<String>> extras;
	LinkedList<String> terminales, generadores;
	ArrayList<ArrayList<String>> gramatics;
	String[][] gramaticaArray;
	int z = 0;
	
	public FNChToFNG(String[][] gramatica) {
		this.terminales = new LinkedList<>();
		this.generadores = new LinkedList<>();
		this.extras = new LinkedHashMap<>();
		this.gramatica = cambiarGeneradores(gramatica);
		this.gramaticaArray = gramatica;
		System.out.println("Gramatica-> " + this.gramatica);
		System.out.println(gramatics);
		System.out.println("Extras-> " + this.extras);
		System.out.println("Terminales-> " + this.terminales);
		System.out.println(this.generadores);
		conversionFNG();
		System.out.println();
		System.out.println(this.gramatica);
	}
	
	private LinkedHashMap<String, ArrayList<String>> cambiarGeneradores(String[][] gramatica) {
		LinkedHashMap<String, ArrayList<String>> convertido = new LinkedHashMap<>();
		gramatics = new ArrayList<>();
		
		for (int i = 0; i < gramatica.length; i++) {
			convertido.put(gramatica[i][0], new ArrayList<String>());
			gramatics.add(new ArrayList<>());
			gramatics.get(i).add(gramatica[i][0]);
			generadores.add(gramatica[i][0]);
			for(int j = 1; j < gramatica[i].length; j++) {
				convertido.get(gramatica[i][0]).add(gramatica[i][j]);
				gramatics.get(i).add(gramatica[i][j]);
				if(gramatica[i][j].length() == 1) {
					if(!terminales.contains(gramatica[i][j])) {
						terminales.add(gramatica[i][j]);
						break;
					}
				}
			}
		}
		return convertido;
	}
	
	private void conversionFNG() {
		for (int i = 0; i < this.gramatics.size(); i++) {
			for (int j = 1; j < this.gramatics.get(i).size(); j++) {
				if(this.gramatics.get(i).get(1).length() != 1) {
					int stop = 0;
//					System.out.println(this.gramatics.get(i).get(0));
//					System.out.println(this.gramatics.get(i).get(j));
					while(comprobacion(this.gramatics.get(i).get(0), Character.toString(this.gramatics.get(i).get(j).charAt(0)))) {
						System.out.println("cadena: "+this.gramatics.get(i).get(1));
						String cambiado = this.gramatics.get(i).get(1).substring(0, 1);
						String temporal = this.gramatics.get(i).get(1).substring(1, this.gramatics.get(i).get(1).length());
						System.out.println("cambiado: "+cambiado);
						System.out.println("temporal: "+temporal);
						sustitucion(cambiado, temporal, this.gramatics.get(i).get(0));
						System.out.println(this.gramatica);
						stop++;
						if(stop == 3) {
							break;
						}
						
						
					}
				}
				
				
			}
		}
	}
	
	private void sustitucion(String aCambiar, String resto, String padre) {
		System.out.println("padre "+padre);
		String res = new String();
		ArrayList<String> listaTemp = new ArrayList<>();
		
		for (int i = 0; i < gramatica.get(aCambiar).size(); i++) {
			res = gramatica.get(aCambiar).get(i) + resto;
			listaTemp.add(res);
		}
		gramatica.replace(padre, listaTemp);
		System.out.println(listaTemp);
		System.out.println("cambiado "+res);
	}
	
	private void recursionIzquierda() {
		
	}
	
	private boolean comprobacion(String generador, String produccion) {
//		System.out.println(generador+","+produccion);
//		System.out.println(generadores.indexOf(generador)+","+generadores.indexOf(produccion));
		if(terminales.contains(generador)) {
			return false;
		}
		if(generadores.indexOf(generador) <= generadores.indexOf(produccion)) {
			return false;
		}
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
