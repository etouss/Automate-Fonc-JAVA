package TP1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.lang.Math;

public class Generateur {
	
	private Integer nombresEtats;
	private Set<Character> alphabet;
	private Random rand = new Random();
	
	public Generateur(String alpha,Integer nb){
		this.nombresEtats = nb;
		alphabet = new HashSet<Character>();
		for(int i = 0;i<alpha.length();i++){
			alphabet.add(alpha.charAt(i));
		}
	}
	
	public Automate generer(){
		ArrayList<Etat> etatList = new ArrayList<Etat>();
		int term0 = Math.abs(rand.nextInt())%5;
		etatList.add(new Etat(true,(term0==0),0));
		for(int i = 1;i<nombresEtats;i++){
			int init = Math.abs(rand.nextInt())%10;
			int term = Math.abs(rand.nextInt())%5;
			//System.out.println(init);
			if(i!=nombresEtats-1)etatList.add(new Etat(init==0,term==0,i));
			else etatList.add(new Etat(init==0,true,i));
		}
		for(int i = 1;i<nombresEtats;i++){
			for(Character l : alphabet){
				int idEtat = Math.abs(rand.nextInt())%(2*nombresEtats);
				if(idEtat<nombresEtats-1)etatList.get(i).ajouteTransition(l,etatList.get(idEtat));
			}
		}
		Automate auto = new Automate();
		for (Etat e : etatList)
			auto.ajouteEtatSeul(e);
		
		return auto;
	}
}

