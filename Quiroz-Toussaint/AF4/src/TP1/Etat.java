package TP1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Etat {

	HashMap<Character, EnsEtat> transitions;
	boolean init;
	boolean term;
	int id;

	 public String toString() {
	        String str = "" + this.id;
	        if (this.isInit()) {
	            str += " initial";
	        }
	        if (this.isTerm()) {
	            str += " terminal";
	        }
	        str += "\n";
	        for (char c : transitions.keySet()) {
	            str+=c;
	            for (Etat e : transitions.get(c)) {
	                str+=" "+e.id;
	            }
	            str+="\n";
	        }
	        return str;
	}
	
	public String singleString(){
		String resultat = "";
		//Iterator<Character> i = transitions.keySet().iterator();
		resultat = "id: "+id+"\n";
		resultat +="term: "+term+"\n";
		resultat +="init: "+init+"\n";
		resultat +="_______________________ \n";
		/*while(i.hasNext()){
			Character j = i.next();
			resultat +="action: "+j+"\n";
			transitions.get(j).toString();
			resultat +="*******************";
		}*/
		return resultat;
	}
	
	public Etat() {
		this.transitions = new HashMap<Character, EnsEtat>();
	}

	public Etat(int id) {
		this.transitions = new HashMap<Character, EnsEtat>();
		this.id = id;
	}

	public Etat(boolean init, boolean term, int id) {
		this.transitions = new HashMap<Character, EnsEtat>();
		this.init = init;
		this.term = term;
		this.id = id;
	}

	public Etat(boolean estInit, boolean estTerm) {
		this.init = estInit;
		this.term = estTerm;
		this.transitions = new HashMap<Character, EnsEtat>();
	}

	public boolean isInit() {
		return init;
	}

	public boolean isTerm() {
		return term;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public void setTerm(boolean term) {
		this.term = term;
	}
	
	public EnsEtat succ(char c){
		EnsEtat ensEtat = new EnsEtat();
		ensEtat = this.transitions.get(c);
		return ensEtat;
	}
	
	public EnsEtat succ(){
		Iterator<Etat> j;
		EnsEtat ensEtat = new EnsEtat();
		Iterator<Character> i = transitions.keySet().iterator();
		while(i.hasNext()){
			j = transitions.get(i.next()).iterator();
			while(j.hasNext()){
				ensEtat.add(j.next());
			}
		}
		return ensEtat;
	}

	
	void ajouteTransition(char c, Etat e){
		
		if(succ(c)!=null){
		transitions.get(c).add(e);
		}
		else{
		EnsEtat ensEtat = new EnsEtat();
		ensEtat.add(e);
		transitions.put(c,ensEtat);
		}
	}
	
	 Set<Character> alphabet(){
		 return  this.transitions.keySet();
	 }
	

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		} else {
			final Etat other = (Etat) obj;
			return (id == other.id);
		}
	}
	
	public Etat copie() {
        return new Etat(this.init, this.term, this.id);
    }
	
	public int getId() {
        return this.id;
    }
	
	public void setId(int id) {
        this.id = id;
    }
	
}
