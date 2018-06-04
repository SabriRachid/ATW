 
package com.serviceRH;
 
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.Key; 
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec; 
 
public class EncryptionFile {
  private static String algo = "Blowfish";
 
  
  public static void deleteFile(String entree) {
	    try {
	      File f = new File(entree);
	      f.delete();
	    }
	    catch (Exception e) {
	      System.out.println("Erreur lors de l'encryptage des donnees");
	    }
	  }
  
  
  public static void crypter(String entree, String sortie) {
    try {
      String password = "attijari";
      byte[] passwordInBytes = password.getBytes("ISO-8859-2"); 
      Key clef = new SecretKeySpec(passwordInBytes, algo); 
      Cipher cipher = Cipher.getInstance(algo);
      cipher.init(Cipher.ENCRYPT_MODE, clef);
 
      byte[] texteClaire = ouvrirFichier(entree);
      byte[] texteCrypte = cipher.doFinal(texteClaire);
      sauverFichier(sortie, texteCrypte);
    }
    catch (Exception e) {
      System.out.println("Erreur lors de l'encryptage des donnees");
    }
  }
 
  public static void decrypter(String entree, String sortie) {
    try {
    	String password = "attijari";
      byte[] passwordInBytes = password.getBytes("ISO-8859-2"); 
      Key clef = new SecretKeySpec(passwordInBytes, algo); 
      Cipher cipher = Cipher.getInstance(algo);
      cipher.init(Cipher.DECRYPT_MODE, clef);
 
      byte[] texteCrypte = ouvrirFichier(entree);
      byte[] texteClaire = cipher.doFinal(texteCrypte);
      sauverFichier(sortie, texteClaire);
    }
    catch (Exception e) {
      System.out.println("Erreur lors du décryptage des donnees");
    }
  }
 
  private static byte[] ouvrirFichier(String filename) {
    try {
      File fichier = new File(filename);
      byte[] result = new byte[(int) fichier.length()];
      FileInputStream in = new FileInputStream(filename);
      in.read(result);
      in.close();
      return result;
    }
    catch (Exception e) {
      System.out.println("Probleme lors de la lecture du fichier: " + e.getMessage());
      return null;
    }
  }
 
  private static void sauverFichier(String filename, byte[] data) {
    try {
      FileOutputStream out = new FileOutputStream(filename);
      out.write(data);
      out.close();
    }
    catch (Exception e) {
      System.out.println("Probleme lors de la sauvegarde du fichier: " + e.getMessage());
    }
  }

}