import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ProyectoAutomata {
	
	public static void crearGrafica(String[][] tabla, String[] lenguaje) {
		try {
			char estadoInicial = '>';
			char estadoFinal = '*';
			
			PrintWriter pw= new PrintWriter(new FileWriter("Automata.txt"));
			pw.println("digraph{");
			for(int i=0; i < tabla.length; i++) {
				for(int j=1; j < tabla[i].length; j++) {
					pw.println("\t" + tabla[i][0] + " -> " + tabla[i][j] + "[label=\"" + lenguaje[j-1] + "\"];");
				}
				
			}
			pw.println("}");
			pw.close();
			}
			catch(IOException e) {
				System.out.println("No se puede escribir en el archivo");
			}
	}

	
	
	
	
	public static void main(String[] args) throws Exception {
		String[] lenguaje = {"a","b"};
		
		String[][] tabla = {{">q0*","q1","q2"},
							{"q1","q2","q0"},
							{"q2","q2","q2"}};
		
		// 0 -> no es nada, 1-> es entrada, 2 -> es final, 3 -> es entrada y final
		int[] arr = {3,0,0};
		
		crearGrafica(tabla, lenguaje);
		
	}
}


