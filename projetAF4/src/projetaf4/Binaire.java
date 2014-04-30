package projetaf4;

import java.util.*;

public class Binaire extends Arbre {

    Arbre gauche;
    Arbre droit;

    public Binaire(Arbre gauche, Arbre droit, char s) {
        this.gauche = gauche;
        this.droit = droit;
        this.symbole = s;
        if (symbole == '+') {
            contientMotVide = droit.contientMotVide || gauche.contientMotVide;
        } else {
            contientMotVide = droit.contientMotVide && gauche.contientMotVide;
        }

        if (symbole == '+' || gauche.contientMotVide) {
            premiers.addAll(droit.premiers);
        }
        if (symbole == '+' || droit.contientMotVide) {
            derniers.addAll(gauche.derniers);
        }
        this.premiers.addAll(gauche.premiers);
        this.derniers.addAll(droit.derniers);
    }

    @Override
    public Map<Feuille, Set<Feuille>> succ() {
        Map retour = gauche.succ();
        retour.putAll(droit.succ());
        if (symbole == '.') {
            for (Feuille f : gauche.derniers) {
                HashSet<Feuille> envoi = (HashSet<Feuille>) retour.get(f);
                envoi.addAll(droit.premiers);
            }
        }
        return retour;
    }

    public String toString() {
        return ("(" + gauche.toString() + " " + this.symbole + " " + droit.toString() + ")");
    }

}
