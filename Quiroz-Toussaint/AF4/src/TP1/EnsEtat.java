package TP1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EnsEtat extends HashSet<Etat> {

	public EnsEtat() {
	}
	
	public String toString() {
        String s = "";
        for (Etat e : this) {
            s += "\n" + e.toString();
        }
        return s;
    }
	
	EnsEtat succ(char c){
		EnsEtat ensEtat = new EnsEtat();
		Iterator<Etat> j = this.iterator();
		while(j.hasNext()){
			Etat etat = j.next();
			if (etat.succ(c) != null){
			ensEtat.addAll(etat.succ(c));
			}
		}
		return ensEtat;
	}
	
	EnsEtat succ(){
		EnsEtat ensEtat = new EnsEtat();
		Iterator<Etat> j = this.iterator();
		while(j.hasNext()){
			ensEtat.addAll(j.next().succ());
		}
		return ensEtat;
	}
	
	boolean contientTerminal(){
		Iterator<Etat> j = this.iterator();
		while(j.hasNext()){
			if(j.next().isTerm()){
				return true;
			}
		}
		return false;
	}
	
	boolean accepte(String s, int i){
		int j = i;
		EnsEtat ensEtat = new EnsEtat();
		ensEtat = this.succ(s.charAt(i));
		for(j = i+1; j< s.length() ;j++){
			ensEtat = ensEtat.succ(s.charAt(j));
		}
		return ensEtat.contientTerminal();
	}
	
	Set<Character> alphabet(){
		Set<Character> resultat = new HashSet<Character>(); 
		Iterator<Etat> j = this.iterator();
		while(j.hasNext()){
			resultat.addAll(j.next().alphabet());
			}
		return resultat;
	}
	
	
	
	
}
