package projetaf4;

import java.util.*;

public class Unaire extends Arbre {

    Arbre fils;

    public Unaire(Arbre f, char s) {
        this.symbole = s;
        this.fils = f;
        contientMotVide = true;
        this.premiers.addAll(this.fils.premiers);
        this.derniers.addAll(this.fils.derniers);
    }

    @Override
    public Map<Feuille, Set<Feuille>> succ() {
        Map retour = fils.succ();
        for (Feuille f : derniers) {
            HashSet<Feuille> envoi = (HashSet<Feuille>) retour.get(f);
            envoi.addAll(premiers);
        }
        return retour;
    }

    public String toString() {
        return ("(" + fils.toString() + ")" + this.symbole);
    }
}
