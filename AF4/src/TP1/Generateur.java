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
	
	/** Initialise le Genrateur Aleatoire
     * 
     * @param alpha 
     * 			String contenant les lettres demande
     * @param nb
     * 			nb d'etats desirees
     */
	public Generateur(String alpha,Integer nb){
		this.nombresEtats = nb;
		alphabet = new HashSet<Character>();
		for(int i = 0;i<alpha.length();i++){
			alphabet.add(alpha.charAt(i));
		}
	}
	/** Creer l'automate associer au generateur.
     * 
     * @return 
     * 			L'automate aleatoire.
     */
	public Automate generer(){
		//On creer un etat initial pour etre sure qu'il y en est au moins 1
		ArrayList<Etat> etatList = new ArrayList<Etat>();
		//Il y a 20% de chance pour que l'etat soit terminal.
		int term0 = Math.abs(rand.nextInt())%5;
		etatList.add(new Etat(true,(term0==0),0));
		//On creer nb Etats.
		for(int i = 1;i<nombresEtats;i++){
			//Il y a 10% de chance pour que l'etat soit initial.
			int init = Math.abs(rand.nextInt())%10;
			//Il y a 20% de chance pour que l'etat soit terminal.
			int term = Math.abs(rand.nextInt())%5;
			if(i!=nombresEtats-1)etatList.add(new Etat(init==0,term==0,i));
			else etatList.add(new Etat(init==0,true,i));
		}
		for(int i = 1;i<nombresEtats;i++){
			for(Character l : alphabet){
				int nbTran = 1+Math.abs(rand.nextInt())%3;
				//Il y a nbTran associé a l.
				for(int j = 0;j<nbTran;j++){
				//Il y a 50% de chance pour qu'une transition existe et qu'elle pointe vers un etat aléatoire.
					int idEtat = Math.abs(rand.nextInt())%(2*nombresEtats);
					if(idEtat<nombresEtats-1)etatList.get(i).ajouteTransition(l,etatList.get(idEtat));
				}
			}
		}
		Automate auto = new Automate();
		for (Etat e : etatList)
			auto.ajouteEtatSeul(e);
		
		return auto;
	}
}

