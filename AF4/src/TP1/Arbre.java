package TP1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

abstract class Arbre{ 
	/*
	 * J'ai preferer rajouter les attributs:
	 * droit
	 * gauche
	 * fils
	 * dans arbre de facon a eviter des cast permanant.
	 * contient1 est une variable plus ou moins equivalent a contient vide utile pour la simplification des expression
	 */
	Character symbole;
	Arbre gauche;
	Arbre droit;
	Arbre fils;
	boolean contientMotVide;
	boolean contient1 = false;
	Set<Feuille> premiers = new HashSet<Feuille>();
    Set<Feuille> derniers = new HashSet<Feuille>();
	
	abstract Map<Feuille,Set<Feuille>> succ();
	/*
	 * Insipire par toString() residuel calcul de facon recursive le residuel d'un arbre
	 * Il traite de facon differentes les cas unaire bianire et feuille.
	 * D'ou le abstract.
	 */
	abstract Arbre residuel(char c,Arbre language);
	/*
	 * La principal difficulte de la methode des residuel reside dans sa tautologie interne.
	 * En effet indirectement son objecif et de savoir si deux expression reguliere son equivalente
	 * Pourtant pour etre appliquer elle nessiste de savoir si deux dits language sont equivalent
	 * En effet un nouvelle etat n'est creer que si le residuel calculer n'existe pas deja.
	 * 
	 *Cette fonction a pour but de reduire les erreurs au minimum en decouvrant des egalite de language.
	 *Cepandant elle n'est pas parfaite:
	 *Example : l'expression : (b(ba)*+(ab)*a)
	 *le residuel b-1 (b(ba)*+(ab)*a) = (ba)*
	 *le residuel a-1 (b(ba)*+(ab)*a) = b(ab)*a+e
	 *
	 *or b(ab)*a+e =  (ba)*
	 *
	 *Cependant le logiciel n'est pas capable de la savoir pour la simple est bonne raison
	 *Que nous avons nous meme du mal a le voir (Andres et Etienne)
	 *
	 *D'autre cas comme celui ci existe probablement.
	 *Je pense qu'a partir du moment ou deux language residuel sont equivalent mais issu d'une construction completement differente le logiciel aura peux de chance de voir leur equivalence.
	 *Une solution aurait etait de verifie avant d'ajouter un etat si le language est bien nouveau par une recurance sur l'ensemble des language deja ajouter avec les residuel
	 *Cependant la complexite d'une tel fonction l'eloignerai de l'algo initial.
	 *
	 *Voir dossier ou oral.
	 *
	 *
	 */
	abstract Arbre simplification(Arbre language );
	
	public abstract String toString();
	public abstract HashSet<String> contientArbre();
	
	
	/** Creer un arbre a partir d'une expression postfixe
     * 
     * @param expresion 
     * 			L'expression ecrit au format postfixe du language
     * @return 
     * 			L'arbre decrivant le language.
     */
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
		Arbre retour = pileArbre.pop().simplification(null);	
		System.out.println(retour);
	return retour;	
	}
	/** Creer l'automate associe a l'arbre
     * 
     * @return 
     * 			L'automate associe a l'arbre.
     */
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
	/** Compare deux arbre en les reduisant d'abord.
     * 
     * @param arbre 
     * 			L'Arbre a compare
     * @return 
     * 			True si les arbres sont equivalent, False sinon
     */
	public boolean egaliteArbre(Arbre arbre){
		Residuel resi1 = new Residuel();
		Residuel resi2 = new Residuel();
		Automate auto1 = resi1.miniResiduel(this);
		Automate auto2 = resi2.miniResiduel(arbre);
		return auto1.egaliteMini(auto2);
		
	}
	
}
