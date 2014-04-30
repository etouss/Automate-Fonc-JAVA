package TP1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

abstract class Arbre{ 
	Character symbole;
	Arbre gauche;
	Arbre droit;
	Arbre fils;
	boolean contientMotVide;
	boolean contient1 = false;
	Set<Feuille> premiers = new HashSet<Feuille>();
    Set<Feuille> derniers = new HashSet<Feuille>();
	
	abstract Map<Feuille,Set<Feuille>> succ();
	abstract Arbre residuel(char c,Arbre language);
	abstract Arbre simplification(Arbre language );
	
	public abstract String toString();
	public abstract HashSet<String> contientArbre();
	
	static Arbre lirePostfixe(String expresion){
		Stack<Arbre> pileArbre = new Stack<Arbre>();
		for (int i = 0; i < expresion.length(); i++){
			switch(expresion.charAt(i)){
				case'+':
				case'.':
					Arbre pop = pileArbre.pop();
					pileArbre.push(new Binaire(pileArbre.pop(),pop,expresion.charAt(i)));
					break;
				case'*':
					pileArbre.push(new Unaire(pileArbre.pop(),expresion.charAt(i)));
					break;
				default :
					pileArbre.push(new Feuille(expresion.charAt(i)));
					break;
			}
		}
	return pileArbre.pop().simplification(null);	
	}
	
	public Automate toAutomate(){
		int i = 0;
		HashMap<Feuille, Etat> map = new HashMap<>();
		Automate auto = new Automate();
		Etat init = new Etat(true,this.contientMotVide,i++);
		for(Feuille f :this.succ().keySet()){
			map.put(f,new Etat(false,this.derniers.contains(f),i++));
		}
		for(Feuille f :this.premiers){
			init.ajouteTransition(f.symbole,map.get(f));
		}
		for(Feuille f:this.succ().keySet()){
			for(Feuille f2:this.succ().get(f)){
					map.get(f).ajouteTransition(f2.symbole,map.get(f2));
			}
		}
		auto.ajouteEtatRecursif(init);
		return auto;
	}
	
	public boolean egaliteArbre(Arbre arbre){
		Residuel resi1 = new Residuel();
		Residuel resi2 = new Residuel();
		Automate auto1 = resi1.miniResiduel(this);
		Automate auto2 = resi2.miniResiduel(arbre);
		return auto1.egaliteMini(auto2);
		
	}
	
}
