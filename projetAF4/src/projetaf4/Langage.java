package projetaf4;

import java.util.Set;
import java.util.HashSet;

class Langage {

	Set<String> mots;

	Langage() {
		this.mots = new HashSet<String>();
	}

	Langage(String chaines[]) {
		this();
		for (int i = 0; i < chaines.length; i++) {
			ajoute(chaines[i]);
		}
	}

	public void ajoute(String u) {
		this.mots.add(u);
	}

	public Langage inter(Langage L) {
		Langage l2 = new Langage();
		for(String s : this.mots){
			if(L.mots.contains(s)) l2.mots.add(s);
		}
		return l2;
	}

	public Langage union(Langage L) {
		Langage l2 = new Langage();
		l2.mots.addAll(this.mots);
		l2.mots.addAll(L.mots);
		return l2;
	}

	public Langage concat(Langage L) {
		Langage l2 = new Langage();
		for(String s : this.mots){
			for(String ss : L.mots){
				l2.mots.add(s+ss);
			}
		}
		return l2;
	}

	public Langage difference(Langage L) {
		Langage l2 = new Langage();
		for(String s : this.mots){
			if(!L.mots.contains(s)) l2.mots.add(s);
		}
		return l2;
	}

	public static String miroirMot(String u) {
		String s = "";
		for(int i = u.length()-1;i>=0;i--){
			s += u.charAt(i);
		}
		return s;
	}

	public Langage miroir() {
		Langage l2 = new Langage();
		for(String s : this.mots){
			l2.mots.add(miroirMot(s));
		}
		return l2;
	}

	public String toString() {
		String res = "{ ";
		for (String u : mots) {
			if (u.length() == 0) {
				res += "mot_vide ";
			} else {
				res += u.toString() + " ";
			}
		}
		return res + "}";
	}

	public Langage residuel(String u){
		Langage l2 = new Langage();
		for(String s : this.mots){
			if(s.startsWith(u)) l2.mots.add(s.substring(u.length()));
		}
		return l2;
	}
	public static String shuffleMot(String u, String v) {
		int i = 0;
		String s = "";
		while(i<u.length() && i< v.length()){
			s+=""+u.charAt(i)+v.charAt(i);
			i++;
		}
		if(i<u.length())s += u.substring(i);
		else if(i<v.length())s += v.substring(i);
		return s;
	}

	public Langage shuffle(Langage L){
		Langage l2 = new Langage();
		for(String s : this.mots){
			for(String s2 : L.mots){
				l2.mots.add(shuffleMot(s,s2));
			}
		}
		return l2;
	}

	public static void main(String args[]) {
		String []s = {"abcd","abgh"};
		String []ss = {"zertyu","zerty"};
		Langage L = new Langage(s);
		Langage l2 = new Langage(ss);
		//System.out.println(L);
		// tester chacune des methodes au fur et a mesure que vous les ecrivez
		//L.ajoute("renaud");
		//System.out.println(L);
		//String s3 = "ab";
		//System.out.println("u = "+s3);
		//System.out.println(miroirMot(s3));
		//Langage l3 = L.concat(l2);
		String re = "aceg";
		//System.out.println(shuffleMot("ccccccccccccccccccccccccccccccccccccccccc","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
		//System.out.println(l3);
		Langage l3 = L.shuffle(l2);
		System.out.println(l3);

	}
}
