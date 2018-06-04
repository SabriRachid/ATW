package cs.dev.facturation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class SeparateurMilliers {

    public static void main(String[] args) {
        /*
        // Created on 02/01/2018
        String NumberOut="";
        double Number =20256314532.53;
        NumberFormat formatter = null;
        formatter=java.text.NumberFormat.getInstance(java.util.Locale.FRENCH);
        //POUR AJOUTER LES SEPARATEUR ENTRE LES MILIERS
        formatter = new DecimalFormat("##,###.##");
        NumberOut = formatter.format(Number).replace(",", ".");
        System.out.println(NumberOut);
        // ***********************************************
        String Str_number ="27 714 344,22";
        System.out.println("Résultat :"+ Str_number.replaceAll(" ", ""));
        // ***********************************************
        System.out.println("********************************************");
        // est un nombre
        System.out.println(isNumeric2("125525202.20dh"));
        System.out.println("--------------------------------------------");
        System.out.println("Code 1");
        System.out.println("--------------------------------------------");
        int i=1;
        for (;i<=5;i++);
        if(i++<=5);
        System.out.printf("%d", i);
        System.out.printf("%d", i);
        System.out.println("\n --------------------------------------------");
        System.out.println("Code 2");
        System.out.println("--------------------------------------------");
        int j=1;
        for (;j<=5;j++);
        if(j++<=5)
        System.out.printf("%d", i);
        System.out.printf("%d", i);
        */
        /*
        System.out.println("------------CUMULE--------------------------------");
        String Cumule="12/4";
        Cumule=Cumule.substring(0,Cumule.length()-2);
        System.out.printf(Cumule);
        
        String string="whatever";
        String reverse = new StringBuffer(string).reverse().toString();
        System.out.println(reverse);
        */
        System.out.println("--------------------------------------------");
        String NomDocument ="DCM17-170118000009-2018";
        String[] List=null;
        String NomD2=null;
        String Chine1=null;
        String NumAttribution=null;
        String TypeDocument=null; 
        
        if(NomDocument.contains("-"))
        {
            NomD2=NomDocument.replace("-", "_");
        }
        if (NomD2.contains("_"))
        {
            List=NomD2.split("_");
            if(List.length==3)
            {
                    Chine1=List[0];                                         //tout les caractiers avant le tiri "_"
                    NumAttribution=List[1];                          //recuperer le deuxieme caractere dans le tableau List "NumAttribution"
                    TypeDocument=List[2];                           //recuperer le troisieme caractere dans le tableau List "TypeDocument"
            }
            System.out.println("C1 : "+ Chine1 + "\n" + "C2 : " + NumAttribution+ "\n" + "C2 : " + TypeDocument);
        }
    }
    public static boolean isNumeric(String str)
    {
        str = str.replaceAll(" ", "");
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
    
    public static boolean isNumeric2(String str2)
    {
        return str2.matches("^(?:(?:\\-{1})?\\d+(?:\\.{1}\\d+)?)$");
    }

}
