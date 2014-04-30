package projetaf4;

import java.util.*;

public class Feuille extends Arbre {

    public Feuille(char s) {
        this.symbole = s;
        contientMotVide = false;
        this.premiers = new HashSet<Feuille>();
        this.premiers.add(this);
        this.derniers.add(this);
    }

    @Override
    public Map<Feuille, Set<Feuille>> succ() {
        Map<Feuille, Set<Feuille>> retour = new HashMap<>();
        retour.put(this, new HashSet<Feuille>());
        return retour;
    }

    public String toString() {
        return ("" + this.symbole);
    }
}
