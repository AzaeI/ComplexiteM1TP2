/**
 * Created by xawirses on 17/10/16.
 */
public class Arrete {
    private Sommet sommet1;
    private Sommet sommet2;

    public Arrete(Sommet sommet1, Sommet sommet2) {
        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
    }

    public int getNameSommet1() {
        return sommet1.getName();
    }

    public int getNameSommet2() {
        return sommet2.getName();
    }
}
