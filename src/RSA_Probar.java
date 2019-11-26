import java.io.File;

public class RSA_Probar {
	public static void main(String[] args) {
		
		File mensajeOriginal = new File("MensajeOriginal.txt");
		File mensajeEncriptado = new File("MensajeEncriptado.txt");
		File mensajeDescifrado = new File("MensajeDescifrado.txt");
		RSA_Desencriptar desencriptar = new RSA_Desencriptar();
		RSA_Encriptar encriptar = new RSA_Encriptar(desencriptar.e, desencriptar.n);

		System.out.println("Encriptando archivo...");
		encriptar.encriptarMensaje(mensajeOriginal, mensajeEncriptado);
		System.out.println("Desencriptando archivo...");
		desencriptar.desencriptarMensaje(mensajeEncriptado, mensajeDescifrado);
	}
}
