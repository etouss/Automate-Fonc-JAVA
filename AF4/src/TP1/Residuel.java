package TP1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class Residuel {

	private Arbre language;
	private Map<Arbre, Etat> map = new HashMap<Arbre, Etat>();
	private Queue<Couple> tmp = new LinkedList<Couple>();
	
	/** Calcul l'automate minimal
     * 
     * @param language 
     * 			L'arbre decrivant le language.
     * @return 
     * 			L'automate minimal asocie au language
     */
	public Automate miniResiduel(Arbre language){
		Etat init = new Etat(true,language.contientMotVide,0);
		this.language = language;
		this.map.put(language,init);
		this.residuelAutomate(language, init);
		Automate auto2 = new Automate();
		auto2.ajouteEtatRecursif(init);
		return auto2;
	}
	/** Calcul les residuels D'un arbre et creer les etat et transition associe.
     * 
     * @param arbre 
     * 			L'arbre dont on calcul les residuels
     * @param etat
     * 			l'etat en cours.
     */
	//La fonction est recursive sur une FIFO de facon a calcule a-1,b-1,aa-1,ab-1
	//plutot que a-1,aa-1,aaa-1,aaaa-1 etc ....
	public void residuelAutomate(Arbre arbre,Etat etat){
		//On recupere l'alpabet associer a l'arbre.
		char[] lettres = new char[arbre.succ().keySet().size()];
		int i = 0;
		for (Feuille f :arbre.succ().keySet()){
			lettres[i++]=f.symbole;	
		}
		for(char l : lettres){
			//Pour chaque lettre on calcul le residuel associer.
			Arbre arbreR=arbre.residuel(l,language);
			boolean empile= true;
			//On verifie si l'arbre calculer a deja un etat qui lui est associe.
			for(Arbre a : this.map.keySet()){
				if(a.toString().equals(arbreR.toString())&&(((a.contient1||a.contientMotVide)&&(arbreR.contient1||arbreR.contientMotVide))||((!a.contientMotVide&&!a.contient1)&&(!arbreR.contient1&&!arbreR.contientMotVide)))){
					//Si l'etat existe deja on ajoute la transition qui convient.
					etat.ajouteTransition(l,map.get(a));
					empile=false;
					break;
				}
			}
			//Si letat n'existe pas encors on le creer on ajoute la transition
			//Enfin on ajoute l'etat et le language a la FIFO permetant la recurance.
			if (empile && arbreR.symbole!='0'){
				Etat e = new Etat(false,arbreR.contientMotVide||arbreR.contient1,map.size());
				etat.ajouteTransition(l,e);
				map.put(arbreR, e);
				if(arbreR.symbole!='1')tmp.add(new Couple(arbreR,e));
			}
		}
		//On declenche la recurance sur la FIFO.
		if(!tmp.isEmpty()){
			Couple c = tmp.remove();
			residuelAutomate(c.a, c.e);
		}
	}
}

