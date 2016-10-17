/**
 * Created by xawirses on 17/10/16.
 */
public class Sommet {
    private int name;
    private Colors color;

    public Sommet(int name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sommet " + (name+1) + ", " + color;
    }

    public int getName() {
        return name;
    }

    public void setColor(Colors color) {
        this.color = color;
    }
}
