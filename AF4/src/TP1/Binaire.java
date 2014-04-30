package TP1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Binaire extends Arbre{ 
	
	public String toString() {
        return ("(" + gauche.toString() + this.symbole + droit.toString() + ")");
    }
	
	public Binaire(Arbre gauche, Arbre droit, char s) {
        this.gauche = gauche;
        this.droit = droit;
        this.symbole = s;
        this.contientMotVide = ((this.gauche.contientMotVide||this.droit.contientMotVide||this.droit.contient1||this.gauche.contient1)&&s=='+') || (((this.gauche.contientMotVide||this.gauche.contient1)&&(this.droit.contientMotVide||this.droit.contient1))&&s=='.');
        if(s=='+'){
        	this.premiers.addAll(this.gauche.premiers);
        	this.premiers.addAll(this.droit.premiers);
        	this.derniers.addAll(this.droit.derniers);
        	this.derniers.addAll(this.gauche.derniers);
        }
        else if(s=='.'){
        	this.premiers.addAll(this.gauche.premiers);
        	this.derniers.addAll(this.droit.derniers);
        	if(this.gauche.contientMotVide)this.premiers.addAll(this.droit.premiers);
        	if(this.droit.contientMotVide)this.derniers.addAll(this.gauche.derniers);
        }
	}
	
	 public Map<Feuille, Set<Feuille>> succ(){
		Map<Feuille, Set<Feuille>> succ = new HashMap<>();
		succ.putAll(gauche.succ());
		succ.putAll(droit.succ());
			if (symbole == '.'){
				for (Feuille f : gauche.derniers) {
		            ((HashSet<Feuille>)succ.get(f)).addAll(droit.premiers);
		        }
			}
		return succ;
		 
	 }

	@Override
	Arbre residuel(char c,Arbre language) {
		Arbre arbre = new Feuille('0');
		if(this.symbole=='.'){
			Arbre arbre1 = new Binaire(gauche.residuel(c,language),droit,'.');
			//System.out.println(arbre+":"+new Binaire(gauche.residuel(c),droit,'.').simplification());
			if(gauche.contientMotVide||gauche.contient1){
				arbre1= new Binaire(arbre1.simplification(language),droit.residuel(c,language),'+');
			}
			//System.out.println(arbre1);
			return arbre1.simplification(language);
		}
		else if(this.symbole=='+'){
			//System.out.println(this+"//"+c+":"+new Binaire(gauche.residuel(c,language),droit.residuel(c,language),'+'));
			return new Binaire(gauche.residuel(c,language),droit.residuel(c,language),'+').simplification(language);
		}
		return arbre;
	}
	

	@Override
	public HashSet<String> contientArbre() {
		HashSet<String> set = new HashSet<String>();
		if(this.symbole == '+'){
			set.addAll(gauche.contientArbre());
			set.addAll(droit.contientArbre());
			return set;
		}
		set.add(this.toString());
		return set;
	}

	@Override
	Arbre simplification(Arbre language) {
		if(this.symbole=='+'){
			if(gauche.symbole == '0' && droit.symbole == '0') return new Feuille('0');
			else if(gauche.symbole == '0')return droit.simplification(language);
			else if(droit.symbole == '0')return gauche.simplification(language);
			else if(gauche.symbole == '1'){
				droit.contient1=true;
				return droit.simplification(language);
			}
			else if(droit.symbole == '1'){
				gauche.contient1=true;
				return gauche.simplification(language);
			}
			for(String S:gauche.contientArbre()){
				if(droit.toString().matches(S))return gauche.simplification(language);
			}
			for(String S:droit.contientArbre()){
				if(gauche.toString().matches(S))return droit.simplification(language);
			}
		}
		else if(this.symbole=='.'){
			if(gauche.symbole == '0' || droit.symbole == '0')return new Feuille('0');
			else if(gauche.symbole == '1') return droit.simplification(language);
			else if(droit.symbole == '1') return gauche.simplification(language);
			else if(gauche.contient1){
				gauche.contient1=false;
				return new Binaire(this,droit,'+').simplification(language);
			}
			else if(droit.contient1){
				droit.contient1=false;
				return new Binaire(this,gauche,'+').simplification(language);
			}
		}
		if(language != null){
			if(this.symbole == '+'){
				if(this.gauche.toString().equals(language.toString()))return gauche;
				if(this.droit.toString().equals(language.toString()))return droit;
			}
			else if(this.symbole == '.'){
				if(this.droit.toString().equals(language.toString())&&this.gauche.contientMotVide)return droit;
			}
		}
		
		return this;
	}
	
}