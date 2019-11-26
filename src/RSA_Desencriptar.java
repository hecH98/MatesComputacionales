import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;
public class RSA_Desencriptar {
	private final int[] numerosPrimos = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
										 101, 103, 107, 109, 113, 127, 131, 137, 139, 149 ,151 ,157, 163, 167, 173, 179, 181 ,191, 193 ,197 ,199,
										 211, 223, 227, 229, 233 ,239, 241, 251, 257 ,263 ,269 ,271, 277, 281, 283, 293, 307 ,311, 313 ,317, 331, 
										 337, 347 ,349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457,
										 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 
										 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733,
										 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 
										 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997};
	
	private int p, q, phi;
	private BigInteger d;
	public BigInteger n, e;
	private BigInteger big;
	
	private Random ran = new Random();
	
	public RSA_Desencriptar() {
		this.p = numerosPrimos[ran.nextInt(numerosPrimos.length)];
		this.q = numerosPrimos[ran.nextInt(numerosPrimos.length)];
		int n = p*q;
		this.n = new BigInteger(n+"");
		this.phi = (this.p-1)*(this.q-1);
		this.e = new BigInteger(getE(this.phi));
		this.d = this.e.modInverse(new BigInteger(Integer.toString(this.phi)+""));
//		System.out.println("p: "+p);
//		System.out.println("q: "+q);
//		System.out.println("n: "+this.n);
//		System.out.println("phi: "+phi);
//		System.out.println("e: "+e);
//		System.out.println("d: "+d);
//		System.out.println();
	}
	
	public int inverso(int e, int phi) {
		e = e%phi;
		for (int i = 1; i < phi; i++) {
			if((e*i) % phi == 1) {
				return i;
			}
		}
		return 1;
	}
	
	public String getE(int phi) {
		int num = ran.nextInt(phi);
		while(true) {
			if(MCD(num, phi) == 1) {
				return Integer.toString(num);
			}
			num = ran.nextInt(phi);
		}
	}
	
	private int MCD(int a, int b) {
		if(b==0) {
			return a;
		}
		else {
			return MCD(b,a%b);
		}
	}
	
	public void desencriptarMensaje(File mensajeEncriptado, File mensajeDescifrado) {
		try {
			BufferedReader br=new BufferedReader(new FileReader(mensajeEncriptado));
			PrintWriter pw= new PrintWriter(new FileWriter(mensajeDescifrado));
			String linea, nuevaLinea = new String(), newChar = new String();
			int intToString=0;
			while((linea=br.readLine())!=null){
				for(int i = 0; i < linea.length(); i++) {
					if(!Character.toString(linea.charAt(i)).equals(" ")) {
						newChar += Character.toString(linea.charAt(i));
					}else {
						intToString = Integer.parseInt(newChar);
						nuevaLinea += (char) desencriptarLetra(intToString);
						newChar = new String();
					}
				}
				pw.println(nuevaLinea);
				nuevaLinea = new String();
			}
			pw.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			System.out.println("No se pudo leer el archivo");
		}
		System.out.println("Archivo desencriptado!");
	}
	
	public long desencriptarLetra(int numero) {
		big = new BigInteger(Integer.toString(numero));
		big = big.modPow(d, n);
		return big.longValue();
	}
}
