package TP1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Residuel {

	private Arbre language;
	private Map<Arbre, Etat> map = new HashMap<Arbre, Etat>();
	private Stack<Couple> tmp = new Stack<>();
	
	
	public Automate miniResiduel(Arbre language){
		Etat init = new Etat(true,language.contientMotVide,0);
		this.language = language;
		this.map.put(language,init);
		this.residuelAutomate(language, init);
		Automate auto2 = new Automate();
		auto2.ajouteEtatRecursif(init);
		return auto2;
	}
	
	public void residuelAutomate(Arbre arbre,Etat etat){
		//System.out.println(etat+""+arbre);

		char[] lettres = new char[arbre.succ().keySet().size()];
		int i = 0;
		for (Feuille f :arbre.succ().keySet()){
			lettres[i++]=f.symbole;	
		}
		for(char l : lettres){
			Arbre arbreR=arbre.residuel(l,language);
			//System.out.println("("+l+" -1) "+arbre.toString()+" = "+arbreR.toString());
			boolean empile= true;
			for(Arbre a : this.map.keySet()){
				//System.out.println(a.toString().equals(arbreR.toString()));
				if(a.toString().equals(arbreR.toString())&&(((a.contient1||a.contientMotVide)&&(arbreR.contient1||arbreR.contientMotVide))||((!a.contientMotVide&&!a.contient1)&&(!arbreR.contient1&&!arbreR.contientMotVide)))){
					etat.ajouteTransition(l,map.get(a));
					empile=false;
					break;
				}
			}
			
			if (empile && arbreR.symbole!='0'){
				Etat e = new Etat(false,arbreR.contientMotVide||arbreR.contient1,map.size());
				etat.ajouteTransition(l,e);
				map.put(arbreR, e);
				if(arbreR.symbole!='1')tmp.push(new Couple(arbreR,e));
			}
		}
		if(!tmp.empty()){
			Couple c = tmp.pop();
			residuelAutomate(c.a, c.e);
		}
	}
}

