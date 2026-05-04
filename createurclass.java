import java.io.*;
import java.util.*;

public class createurclass {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Entrez le nom de la classe");
        String nom = myObj.nextLine();

        System.out.println("Entrez le nombre d'argument(s)");
        int nbrargs = myObj.nextInt();

        List<String> attributs = new ArrayList<>();
        List<String> typeattributs = new ArrayList<>();

        myObj.nextLine();
        for (int i = 0; i < nbrargs; i++) {
            System.out.println("Quel est le nom de l'attribut ?");
            attributs.add(myObj.nextLine());
            System.out.println("Quel est le type de l'attribut ? (int, String, float, double, boolean...)");
            typeattributs.add(myObj.nextLine());
        }

        try (FileWriter writer = new FileWriter(nom + ".java")) {

            // En-tête
            writer.write("import java.io.*;\nimport java.util.*;\n\npublic class " + nom + " {\n\n");

            // Attributs
            for (int i = 0; i < nbrargs; i++) {
                writer.write("    private " + typeattributs.get(i) + " " + attributs.get(i) + ";\n");
            }
            writer.write("\n");

            // Constructeur par défaut
            writer.write("    public " + nom + "() {\n");
            for (int i = 0; i < nbrargs; i++) {
                String valDefaut = valeurDefaut(typeattributs.get(i));
                writer.write("        this." + attributs.get(i) + " = " + valDefaut + ";\n");
            }
            writer.write("    }\n\n");

            // Constructeur paramétré
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < nbrargs; i++) {
                if (i > 0) params.append(", ");
                params.append(typeattributs.get(i)).append(" ").append(attributs.get(i));
            }
            writer.write("    public " + nom + "(" + params + ") {\n");
            for (int i = 0; i < nbrargs; i++) {
                writer.write("        this." + attributs.get(i) + " = " + attributs.get(i) + ";\n");
            }
            writer.write("    }\n\n");

            // Getters
            for (int i = 0; i < nbrargs; i++) {
                String nomMaj = Character.toUpperCase(attributs.get(i).charAt(0)) + attributs.get(i).substring(1);
                writer.write("    public " + typeattributs.get(i) + " get" + nomMaj + "() {\n");
                writer.write("        return this." + attributs.get(i) + ";\n");
                writer.write("    }\n\n");
            }

            // Setters
            for (int i = 0; i < nbrargs; i++) {
                String nomMaj = Character.toUpperCase(attributs.get(i).charAt(0)) + attributs.get(i).substring(1);
                writer.write("    public void set" + nomMaj + "(" + typeattributs.get(i) + " " + attributs.get(i) + ") {\n");
                writer.write("        this." + attributs.get(i) + " = " + attributs.get(i) + ";\n");
                writer.write("    }\n\n");
            }

            // toString
            writer.write("    @Override\n    public String toString() {\n");
            writer.write("        return \"" + nom + "{\" +\n");
            for (int i = 0; i < nbrargs; i++) {
                String sep = (i == 0) ? "" : ", ";
                boolean isString = typeattributs.get(i).equals("String");
                String valStr = isString
                        ? "\"\\\"\" + " + attributs.get(i) + " + \"\\\"\""
                        : attributs.get(i);
                writer.write("            \"" + sep + attributs.get(i) + "=\" + " + valStr + " +\n");
            }
            writer.write("            \"}\";\n    }\n\n");

            // Fermeture classe
            writer.write("}\n");

            System.out.println("Fichier " + nom + ".java généré avec succès.");

        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier.");
            e.printStackTrace();
        }
    }

    private static String valeurDefaut(String type) {
        return switch (type) {
            case "int"     -> "0";
            case "float"   -> "0.0f";
            case "double"  -> "0.0";
            case "boolean" -> "false";
            case "char"    -> "'\\0'";
            default        -> "\"\"";  // String et autres
        };
    }
}