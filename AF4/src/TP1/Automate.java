package TP1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Automate extends EnsEtat {

    private EnsEtat initiaux;

    public Automate() {
        super();
        initiaux = new EnsEtat();
    }
    
    public String toString() {
        String s = "" + this.size() + " Etats\n";
        s+=super.toString();
        return s;
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
    
    
    public Automate determinise() {
        if (this.estDeterministe()) {
            return this.copie();
        }
        Automate copie = this.copie();
        Automate deterministe = new Automate();
        HashMap<EnsEtat, Etat> corres = new HashMap<EnsEtat, Etat>();
        Stack<EnsEtat> pile = new Stack<EnsEtat>();

        Etat ei = new Etat(true, copie.initiaux.contientTerminal());
        corres.put(copie.initiaux, ei);
        deterministe.add(ei);
        pile.push(copie.initiaux);
        int i = 1;
        while (!pile.isEmpty()) {
            EnsEtat courant = pile.pop();
            for (Character c : copie.alphabet()) {
                EnsEtat suc = courant.succ(c);
                if (!corres.containsKey(suc)) {
                    Etat e = new Etat(false, suc.contientTerminal(), i++);
                    corres.put(suc, e);
                    deterministe.add(e);
                    pile.push(suc);
                }
                corres.get(courant).ajouteTransition(c, corres.get(suc));
            }
        }
        return deterministe;
    }
    
    public Automate copie() {
        Automate auto = new Automate();
        HashSet<Etat> set = new HashSet<Etat>();
        for (Etat i : this) {
            Etat e = i.copie();
            set.add(e);
        }
        for (Etat e : set) {
            for (Etat i : this) {
                if (e.getId() == i.getId()) {
                    for (char c : i.transitions.keySet()) {
                        for (Etat f : i.transitions.get(c)) {
                            for (Etat g : set) {
                                if (g.getId() == f.getId()) {
                                    e.ajouteTransition(c, g);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Etat e : set) {
            auto.ajouteEtatSeul(e);
        }
        return auto;
    }
    
    public Automate complete() {
        Automate auto = this.copie();
        Etat pb = new Etat(false, false, -1);
        auto.ajouteEtatSeul(pb);
        for (Etat e : auto) {
            for (char c : auto.alphabet()) {
                if (!e.transitions.keySet().contains(c)) {
                    e.ajouteTransition(c, pb);
                }
            }
        }
        return auto;
    }
    
    public Automate complementaire() {
        Automate auto = this.complete().determinise();
        for (Etat e : auto) {
            e.setTerm(!e.isTerm());
        }
        return auto;
    }
    
    public Automate miroir() {
        Automate auto = new Automate();
        //HashSet<Etat> set = new HashSet<Etat>();
        HashMap<Etat, Etat> map = new HashMap<Etat, Etat>();
        for (Etat e : this) {
            Etat f = new Etat(e.isTerm(), e.isInit(), e.getId());
            map.put(e, f);
            auto.add(f);
        }
        for (Etat e : this) {
            for (char c : e.transitions.keySet()) {
                for (Etat etat : e.succ(c)) {
                    map.get(etat).ajouteTransition(c, map.get(e));
                }
            }
        }
        return auto;
    }   
    
    public Automate union(Automate a) {
        Automate cp = this.determinise();
        Automate cp2 = a.determinise();
        
        for (Etat e : cp2){
        	e.setId(e.getId()+cp.size());
        }

        Automate union = cp;
        for (Etat e : cp2.initiaux) {
        	union.ajouteEtatRecursif(e);
        }
        return union.determinise();
    }
    
    public Automate intersection(Automate a) {
        return this.complementaire().union(a.complementaire()).complementaire();
    }
    
    public void toFile(String path) {
        File f = new File(path);
        try {
            PrintStream ps = new PrintStream(f);
            ps.print(this.toString());
            ps.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erreur");
        }
    }
    
    
    public Automate(String path) {
        super();
        initiaux = new EnsEtat();
        Scanner sc;
        File f = new File(path);
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException ex) {
            System.out.println("Erreur");
            return;
        }
        
        int nb = sc.nextInt();
        HashMap<Integer,Etat> set = new HashMap<Integer,Etat>();
        for(int i =0;i<nb;i++){
            set.put(i,new Etat(i));
        }
        sc.nextLine();
        sc.nextLine();

        for(int i =0;i<nb;i++){
            String str = sc.nextLine();
            String[] nombre = str.split(" ",2);
            int NumeroEtat = Integer.parseInt(nombre[0]);
            System.out.print(NumeroEtat);
            if(str.contains("initial")) set.get(NumeroEtat).setInit(true);
            if(str.contains("terminal")) set.get(NumeroEtat).setTerm(true);
            while(!str.equals("") &&  sc.hasNextLine()){
                str = sc.nextLine();
                String [] tab= str.split(" ");
                for(int j =1;j<tab.length;j++){
                    set.get(NumeroEtat).ajouteTransition(tab[0].charAt(0),set.get(Integer.parseInt(tab[j])));

                }
            }
            //System.out.println(set.get(i).toString());
        }
        for(Etat e : set.values()){
            this.ajouteEtatSeul(e);
        }
    }
    
    public Automate suppressPoubelle(){
    	Automate cpy = this.copie();
    	Etat etatBin = null;
    	for (Etat e : cpy){
    		boolean bin = false;
    		if(!e.isTerm()){
    			bin = true;
    			for(Etat e2 : e.succ()){
    				bin = bin && e2==e;
    			}
    		}
    		if(bin)etatBin = e;
    	}
    	if(etatBin != null){
    		cpy.remove(etatBin);
    		for(Etat e : cpy){
    			HashSet<Character>supr = new HashSet<Character>();
    			for(Character c : e.transitions.keySet()){
    				e.transitions.get(c).remove(etatBin);
    				if(e.transitions.get(c).size()==0)supr.add(c);
    			}
    			for(Character c : supr){
    				e.transitions.remove(c);
    			}
    		}
    	}
    	return cpy;
    }
    
    public boolean egaliteAuto(Automate auto){
    	Moore moore1 = new Moore();
    	Moore moore2 = new Moore();
    	Automate auto1 = moore1.miniMoore(this);
    	Automate auto2 = moore2.miniMoore(auto);
    	
    	return auto1.egaliteMini(auto2);
    }
    
    private String parcoursRecursif(Character[] lettresList,HashSet<Etat> etatsParcourus,Etat etat,String result){
    	for(Character c:lettresList){
    		Etat etatSuivant = new Etat();
    		if(etat.transitions.get(c)!=null && etat.transitions.size()!=0 &&etat.transitions.get(c).size()!=0){
    			for (Etat e : etat.transitions.get(c))etatSuivant = e;
    			//System.out.println(result);
    			result = result.concat(c.toString());
    			if(etatSuivant.isTerm())result = result.concat("T");
    			if(!etatsParcourus.contains(etatSuivant)){
    				etatsParcourus.add(etatSuivant);
    				result = this.parcoursRecursif(lettresList, etatsParcourus ,etatSuivant, result);
    			}
    		}
    	}
    	
    	return result;
    }
    
    public String parcours(){
    	HashSet<Character> lettres = new HashSet<Character>();
    	HashSet<Etat> etatsParcourus = new HashSet<Etat>(); 
		for (Etat e : this)lettres.addAll(e.transitions.keySet());
		Character[] lettresList = new Character[lettres.size()];
		for(int i=0;i<lettres.size();i++)lettresList[i] = (Character) lettres.toArray()[i];
    	String result = "";
    	Etat init1 = new Etat();
    	for(Etat e:this.initiaux)init1=e;
    	etatsParcourus.add(init1);
    	result = parcoursRecursif(lettresList,etatsParcourus,init1,result);
    	//System.out.println(result);
    	return result;
    }

	public boolean egaliteMini(Automate auto) {
		String result1 = this.parcours();
		String result2 = auto.parcours();
		//System.out.println(result1);
		//System.out.println(result2);
		return result1.matches(result2);
	}
    
}
