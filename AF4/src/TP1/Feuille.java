package TP1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Feuille extends Arbre{
	static int etat = 0;
	public String toString() {
        return ("" + this.symbole);
    }
	
	public Feuille(char s) {
        this.symbole = s;
        this.contientMotVide = false;
        if(this.symbole == '1')this.contientMotVide=true;
        this.premiers.add(this);
        this.derniers.add(this);
    }
	
	@Override
	public Map<Feuille,Set<Feuille>> succ(){
		Map<Feuille, Set<Feuille>> succ = new HashMap<>();
        succ.put(this, new HashSet<Feuille>());
        return succ;
	}

	@Override
	Arbre residuel(char c,Arbre language) {
		return c == this.symbole ? new Feuille('1'):new Feuille('0');
	}

	@Override
	public HashSet<String> contientArbre() {
		HashSet<String> set = new HashSet<String>();
		set.add(this.toString());
		return set;
	}

	@Override
	Arbre simplification(Arbre language) {
		// TODO Auto-generated method stub
		return this;
	}
	
}