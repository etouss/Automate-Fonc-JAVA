package TP1;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Etat etat0 = new Etat(true,false,1);
		Etat etat1 = new Etat(false,false,2);
		Etat etat2 = new Etat(false, true, 3);
	
		etat1.ajouteTransition('b', etat2);
		etat1.ajouteTransition('c', etat1);
		etat1.ajouteTransition('a', etat1);
		etat0.ajouteTransition('a', etat1);
		
		
		Automate automate = new Automate();
		automate.ajouteEtatRecursif(etat0);
		
		Automate automate2 = automate.determinise();
		
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println(automate.toString());
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println(automate2.toString());
		
		
		
		
		

	}

}
