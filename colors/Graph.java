import java.io.*;
import java.util.ArrayList;

/**
 * Created by Michael,Yohan on 17/10/16.
 */
public class Graph {
    int nbSommets;
    int nbArretes;
    private Sommet sommets[];
    private Arrete arretes[];
    private boolean sat;
    private String modele;

    public Graph(String[] args) {
        nbSommets = Integer.parseInt(args[0]);
        nbArretes = Integer.parseInt(args[1]);

        sommets = new Sommet[nbSommets];
        arretes = new Arrete[nbArretes];

        for (int i = 0; i < nbSommets; i++)
            sommets[i] = new Sommet(i);

        for (int i = 0; i < nbArretes; i++)
            arretes[i] = new Arrete(
                    sommets[Integer.parseInt(args[i*2+2])-1],
                    sommets[Integer.parseInt(args[i*2+3])-1]);

        dimacs();
        startMinisat();
        analyseOutput();
        colorGraph();
        if(sat)
            affiche();
    }


    private void dimacs() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("colors.cnf")));

            writer.write("p cnf " + (4*nbSommets+3*nbArretes) +" " + 3*nbSommets + "\n");
            for (int i = 0; i < nbSommets; i++) {
                writer.write((i*3+1) + " " + (i*3+2) + " " + (i*3+3) + " 0\n");
                writer.write("-" + (i*3+1) + " -" + (i*3+2) + " 0\n");
                writer.write("-" + (i*3+1) + " -" + (i*3+3) + " 0\n");
                writer.write("-" + (i*3+2) + " -" + (i*3+3) + " 0\n");
            }

            for (int i = 0; i < nbArretes; i++) {
                writer.write("-" + (arretes[i].getNameSommet1()*3+1) + " -" + (arretes[i].getNameSommet2()*3+1) + " 0\n");
                writer.write("-" + (arretes[i].getNameSommet1()*3+2) + " -" + (arretes[i].getNameSommet2()*3+2) + " 0\n");
                writer.write("-" + (arretes[i].getNameSommet1()*3+3) + " -" + (arretes[i].getNameSommet2()*3+3) + " 0\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startMinisat() {
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("./minisat colors.cnf output");
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void analyseOutput() {
        String ligne = "";
        BufferedReader ficTexte;
        try {
            ficTexte = new BufferedReader(new FileReader(new File("output")));
            if (ficTexte == null) {
                throw new FileNotFoundException("Fichier non trouvé: output");
            }

            ligne = ficTexte.readLine();
            if (ligne != null) {
                if(ligne.equals("SAT")) {
                    sat = true;
                    System.out.println("SAT");
                } else {
                    System.out.println("INSAT");
                    sat = false;
                }
            }

            if(sat && ficTexte != null) {
                ligne = ficTexte.readLine();
                if (ligne == null) {
                    System.out.println("Erreur de récupération du modèle");
                    ficTexte.close();
                    System.exit(1);
                }
                modele = ligne;
            }

            ficTexte.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void colorGraph() {
        for (String retval: modele.split(" ")) {
            int i = Integer.parseInt(retval);
            if (i > 0) {
                int s = (int)Math.ceil((float)i/3);
                int c = i%3;
                if(c == 1)
                    sommets[s-1].setColor(Colors.RED);
                else if (c == 2)
                    sommets[s-1].setColor(Colors.GREEN);
                else
                    sommets[s-1].setColor(Colors.BLUE);
            }
        }
    }

    private void affiche() {
        for (int i = 0; i < nbSommets; i++)
            System.out.println(sommets[i].toString());
    }


    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Aucune entrée détecté");
            System.exit(1);
        }

        if(args.length != Integer.parseInt(args[1])*2+2) {
            System.out.println("Entrée incorecte");
            System.exit(1);
        }

        Graph graph = new Graph(args);
    }
}
