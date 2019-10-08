import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class CrearAutomata {
	private static int n;
	private static Character[] alfabeto;
	private static LinkedList<LinkedList<Integer>[]> estados;
	public static void main(String[] args) {
		String expresion = JOptionPane.showInputDialog("Ingresa la expresion regular").replaceAll(" ", "");
		alfabeto = getAlfabeto(expresion);
		n = alfabeto.length;
		estados = new LinkedList<>();
		estados.add(new LinkedList[n]);
		estados.add(new LinkedList[n]);
		exprToAFN(0, 1, expresion);
		
		imprimeLinkedList(estados);
	}
	
	public static void exprToAFN(int inicio, int fin, String str) {
		System.out.println(str);
		// Elimina () si rodean por completo al str (str)
		str = eliminaBrackets(str);
		// Caso base
		if(str.length()==1) {
			LinkedList<Integer>[] current = estados.get(inicio);
			for(int i=0;i<alfabeto.length;i++) {
				if(alfabeto[i]==str.charAt(0)) {
					if(current[i]==null) {
						current[i] = new LinkedList<Integer>();
					}
					current[i].add(fin);
					break;
				}
			}
			return;
		}
		// String donde adentro de parentesis este con espacios
		String cleanStr = getCleanStr(str);
		// Caso | (Union)
		if(cleanStr.contains("|")) {
			System.out.println("union");
			exprToAFN(inicio,fin,str.substring(0,cleanStr.indexOf('|')));
			exprToAFN(inicio,fin,str.substring(cleanStr.indexOf('|')+1,str.length()));
			return;
		}
		// Caso concatenacion
		int x = 0;
		String reset = "| "; // Operaciones
		String ignorar = "+*";
		for(int i=0;i<cleanStr.length();i++) {
			// Si es operacion o cadena
			if(reset.contains(""+cleanStr.charAt(i))) x = 0;
			else if(!ignorar.contains(""+cleanStr.charAt(i))) x++;
			// Si hay concatenacion
			if(x==2) {
				System.out.println("concatenacion");
				estados.add(new LinkedList[n]);
				x=estados.size()-1;
				exprToAFN(inicio,x,str.substring(0,i));
				exprToAFN(x,fin,str.substring(i,str.length()));
				return;
			}
		}
		// Caso cerradura positiva
		if(cleanStr.contains("+")) {
			System.out.println("cerradura positiva");
			estados.add(new LinkedList[n]);
			x=estados.size()-1;
			exprToAFN(inicio,x,str.substring(0,cleanStr.indexOf('+')));
			exprToAFN(x,x,str.substring(0,cleanStr.indexOf('+')));
			exprToAFN(x,fin,"$");
			return;
		}
		// Caso cerradura
		if(cleanStr.contains("*")) {
			System.out.println("cerradura");
			estados.add(new LinkedList[n]);
			x=estados.size()-1;
			exprToAFN(inicio,x,"$");
			exprToAFN(x,x,str.substring(0,cleanStr.indexOf('*')));
			exprToAFN(x,fin,"$");
			return;
		}
		System.out.println("no hizo nada el ciclo");
	}
	
	private static Character[] getAlfabeto(String str) {
		char[] removes = {'(',')','*','+','|'};
		for(int i=0;i<removes.length;i++) {
			str = str.replace(removes[i], ' ');
		}
		str = str.replaceAll(" ", "");
		LinkedList<Character> c = new LinkedList<>();
		for(int i=0;i<str.length();i++) {
			boolean insert = true;
			Iterator<Character> cs = c.iterator();
			while(cs.hasNext()) {
				if(cs.next()==str.charAt(i)) {
					insert=false;
					break;
				}
			}
			if(insert) c.add(str.charAt(i));
		}
		c.add('$');
		return c.toArray(new Character[c.size()]);
	}
	
	private static String eliminaBrackets(String str) {
		// Se asegura que el bracket ( al inicio esta conectado con ) al final
		if(str.startsWith("(") && str.endsWith(")")) {
			// Profundidad de entre Brackets
			int pb=1;
			for(int i=1;i<str.length()-1;i++) {
				if(str.charAt(i)==('(')) pb++;
				else if(str.charAt(i)==(')')) pb--;
				if(pb==0) return str;
			}
			return str.substring(1,str.length()-1);
		}
		return str;
	}
	
	private static String getCleanStr(String str) {
		String cleanStr = "";
		int pb=0;
		for(int i=0;i<str.length();i++) {
			if(str.charAt(i)==('(')) {
				pb++;
				cleanStr+=pb==1?'(':' ';
			}
			else if(str.charAt(i)==(')')) {
				pb--;
				cleanStr+=pb==0?')':' ';
			}
			else cleanStr+=pb==0?str.charAt(i):' ';
		}
		return cleanStr;
	}
	
	private static void imprimeLinkedList(LinkedList<LinkedList<Integer>[]> list) {
		Iterator<LinkedList<Integer>[]> iterator = list.iterator();
		int q=0;
		while(iterator.hasNext()) {
			LinkedList<Integer>[] current = iterator.next();
			System.out.print("Q"+q+": ");
			for(int i=0;i<current.length;i++) {
				if(current[i] == null) {
					System.out.print(alfabeto[i]+"{}, ");
				}
				else {
					System.out.print(alfabeto[i]+"{");
					Iterator<Integer> transitions = current[i].iterator();
					if(transitions.hasNext()) System.out.print(transitions.next());
					while(transitions.hasNext()) {
						System.out.print(", " + transitions.next());
					}
					System.out.print("}, ");
				}
			}
			System.out.println();
			q++;
		}
	}
	
	
}

