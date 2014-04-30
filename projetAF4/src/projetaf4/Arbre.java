package projetaf4;

import java.util.*;

public abstract class Arbre {

    boolean contientMotVide = false;//Ɛ
    char symbole;
    Set<Feuille> premiers = new HashSet<Feuille>();
    Set<Feuille> derniers = new HashSet<Feuille>();
    abstract Map<Feuille,Set<Feuille>> succ();

    public abstract String toString();

    public Arbre lirePostfixe(String post) {
        Stack<Arbre> pile = new Stack<Arbre>();

        for (int i = 0; i < post.length(); i++) {
            switch (post.charAt(i)) {
                case '+':
                case '.':
                    pile.push(new Binaire(pile.pop(), pile.pop(), post.charAt(i)));
                    break;
                case '*':
                    pile.push(new Unaire(pile.pop(), post.charAt(i)));
                    break;

                default:
                    pile.push(new Feuille(post.charAt(i)));
            }
        }
        return pile.pop();
    }

}
