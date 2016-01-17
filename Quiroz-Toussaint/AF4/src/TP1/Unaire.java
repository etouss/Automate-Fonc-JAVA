package TP1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class Unaire extends Arbre{
	
	public String toString() {
        return ("(" + fils.toString() + ")" + this.symbole);
    }
	
	public Unaire(Arbre fils, char s) {
        this.symbole = s;
        this.fils = fils;
        this.contientMotVide= true;
        this.premiers.addAll(this.fils.premiers);
        this.derniers.addAll(this.fils.derniers);
    }
	
    public Map<Feuille, Set<Feuille>> succ() {
    	Map<Feuille, Set<Feuille>> succ = fils.succ();
        for (Feuille f : derniers) {
            ((HashSet<Feuille>)succ.get(f)).addAll(premiers);
        }
        return succ;
    }
    /** Calcul le residuel d'un Unaire.
     * 
     * @param language 
     * 			L'arbre de base
     * @param c
     * 			Le char par rapport auquel on calcul le residuel
     * @return 
     * 			L'arbre decrivant le residuel du language
     */
	@Override
	Arbre residuel(char c,Arbre language) {
		/*
		 * Applique les regle de calcul des residuel sur les Unaire.
		 */
		if((fils.residuel(c,language).toString().equals(fils.toString())))
			return this.simplification(language);
		else if(fils.residuel(c,language).contientMotVide)
			return this.simplification(language);
		else 
			return new Binaire(fils.residuel(c,language),this,'.').simplification(language); 
	}
	/** Simplifie les Unaire.
     * 
     * @param language 
     * 			L'arbre de base
     * @return 
     * 			L'arbre simplifie
     */
	Arbre simplification(Arbre language){
		// a** = a*
		if(fils.symbole == '*')return fils.simplification(language);
		else if(fils.symbole == '+'){
			if(fils.gauche.symbole=='*'){
				Arbre a1 = new Binaire(fils.gauche.fils.simplification(language),fils.droit.simplification(language),'+');
				Arbre a2 = new Unaire(a1.simplification(language),'*');
				return a2.simplification(language);
			}
			if(fils.droit.symbole=='*'){
				Arbre a1 = new Binaire(fils.droit.fils.simplification(language),fils.gauche.simplification(language),'+');
				Arbre a2 = new Unaire(a1.simplification(language),'*');
				return a2.simplification(language);
			}
		}
		return this;
	}

	@Override
	public HashSet<String> contientArbre() {
		HashSet<String> set = new HashSet<String>();
		if(fils.symbole == '+'){
			for(String s : fils.contientArbre())set.add(s.concat(this.symbole.toString()));
		}
		set.add(this.toString());
		return set;
	}
}
