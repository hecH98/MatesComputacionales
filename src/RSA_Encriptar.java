import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class RSA_Encriptar {
	private BigInteger n, big, e;

	public RSA_Encriptar(BigInteger e, BigInteger n) {
		this.e = e;
		this.n = n;
	}
	
	public void encriptarMensaje(File mensajeOriginal, File mensajeEncriptado) {
		try {
			BufferedReader br=new BufferedReader(new FileReader(mensajeOriginal));
			PrintWriter pw= new PrintWriter(new FileWriter(mensajeEncriptado));
			String linea, nuevaLinea = new String();
			int intToChar=0;
			while((linea=br.readLine())!=null){
				for(int i = 0; i < linea.length(); i++) {
					intToChar = linea.charAt(i);
					nuevaLinea += encriptarLetra(intToChar) + " ";
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
		System.out.println("Archivo encriptado!");
	}

	public String encriptarLetra(int numero) {
		big = new BigInteger(Integer.toString(numero));
		big = big.modPow(this.e, this.n);
		return big.toString();
	}
}
