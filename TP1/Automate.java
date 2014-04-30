package TP1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Automate extends EnsEtat {

    private EnsEtat initiaux;

    public Automate() {
        super();
        initiaux = new EnsEtat();
    }
    
    public String toString(){
    	return super.toString();
    }

    boolean ajouteEtatSeul(Etat e){
    	
    	if(e.isInit()){
			this.getInitiaux().add(e);
		}
    	
    	if(this.contains(e))
    	{
    	return false;
    	}
    	else{
    	this.add(e);
    	return true;
    	}
    }
    
    
    boolean ajouteEtatRecursif(Etat e){
    	if(ajouteEtatSeul(e)){
    		EnsEtat ensEtat = e.succ();
    		Iterator<Etat> j = ensEtat.iterator();
    		while(j.hasNext()){
    			ajouteEtatRecursif(j.next());
    		}
    		return true;
    	}
    	return false;
    }
    
    boolean estDeterministe(){
    	Iterator<Etat> i = this.iterator();
    	Iterator<Character> j;
    	boolean resultat = true;
    	while(i.hasNext()){
    		Etat etat = i.next();
    		j = etat.transitions.keySet().iterator();
    		while(j.hasNext()){
    			EnsEtat ensEtat = etat.transitions.get(j.next());
    			if(ensEtat.size()>1){
    				resultat = false;
    			}
    		}
    	}
    	return resultat;
    }
    
    public EnsEtat getInitiaux() {
        return initiaux;
    }
    
    boolean accepte(String s){
    	return this.initiaux.accepte(s, 0);
    }
    
    Automate determinise(){
    	int i = 0;
    	Automate resultat = new Automate();
    	HashMap<EnsEtat,Etat> deter = new HashMap<EnsEtat,Etat>();
    	deter.put(this.initiaux,new Etat(true,this.initiaux.contientTerminal(),0));
    	resultat.ajouteEtatSeul(new Etat(true,this.initiaux.contientTerminal(),0));
    	Iterator<EnsEtat> e = deter.keySet().iterator();
    	while(e.hasNext()){
    		Set<Character> action = e.next().alphabet();
        	Iterator<Character> j = action.iterator();
        	while(j.hasNext()){
        		i ++;
        		Character c = j.next();
        		EnsEtat ensEtat = this.initiaux.succ(c);
        		Etat etat = new Etat(false,ensEtat.contientTerminal(),i);
        		deter.put(ensEtat, etat);
        		resultat.ajouteEtatSeul(etat);
        		
        		System.out.println(deter);
        	}
    	}
    	resultat.ajouteEtatRecursif(deter.get(this.initiaux));
    	return resultat;
    }
    
}
