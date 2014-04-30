package projetaf4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Stack;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Automate extends EnsEtat {

    private EnsEtat initiaux;

    public Automate() {
        super();
        initiaux = new EnsEtat();
    }

    public Automate(String path) {
        super();
        initiaux = new EnsEtat();
        Scanner sc;
        File f = new File(path);
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException ex) {
            System.out.println("Erreur de lecture");
            return;
        }
        int nb = sc.nextInt();
        System.out.println(nb);
        HashMap<Integer,Etat> set = new HashMap<>();
        for(int i =0;i<nb;i++){
            set.put(i,new Etat(i));
        }
        sc.nextLine();
        sc.nextLine();
        
        for(int i =0;i<nb;i++){
            
            String str = sc.nextLine();
            if(str.contains("initial")) set.get(i).setInit(true);
            if(str.contains("terminal")) set.get(i).setTerm(true);
            while(!str.equals("") &&  sc.hasNextLine()){
                str = sc.nextLine();
                String [] tab= str.split(" ");
                for(int j =1;j<tab.length;j++){
                    set.get(i).ajouteTransition(tab[0].charAt(0),set.get(Integer.parseInt(tab[j])));
                    
                }
            }
            System.out.println(set.get(i).toString());
        }
        for(Etat e : set.values()){
            this.ajouteEtatSeul(e);
        }
    }

    public EnsEtat getInitiaux() {
        return initiaux;
    }

    public boolean ajouteEtatSeul(Etat e) {
        if (this.contains(e)) {
            return false;
        }
        if (e.isInit()) {
            this.initiaux.add(e);
        }
        this.add(e);
        return true;

    }

    public boolean ajouteEtatRecursif(Etat e) {
        if (ajouteEtatSeul(e)) {
            for (EnsEtat ee : e.transitions.values()) {
                for (Etat eee : ee) {
                    ajouteEtatRecursif(eee);
                }
            }
            return true;
        }
        return false;
    }

    public boolean estDeterministe() {
        if (this.initiaux.size() > 1) {
            return false;
        }
        for (Etat e : this) {
            for (EnsEtat ee : e.transitions.values()) {
                if (ee.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        String s = "" + this.size() + " Etats\n";
        for (Etat e : this) {
            s += "\n" + e.toString();
        }
        return s;
    }

    public Automate determinise() {
        if (this.estDeterministe()) {
            return this;
        }
        Automate d = new Automate();
        HashMap<EnsEtat, Etat> corres = new HashMap<EnsEtat, Etat>();
        Stack<EnsEtat> pile = new Stack<EnsEtat>();

        Etat ei = new Etat(true, this.initiaux.contientTerminal());
        corres.put(this.initiaux, ei);
        d.add(ei);
        pile.push(initiaux);
        int i = 1;
        while (!pile.isEmpty()) {
            EnsEtat courant = pile.pop();
            for (Character c : this.alphabet()) {
                EnsEtat suc = courant.succ(c);
                if (!corres.containsKey(suc)) {
                    Etat e = new Etat(false, suc.contientTerminal(), i++);
                    corres.put(suc, e);
                    d.add(e);
                    pile.push(suc);
                }
                corres.get(courant).ajouteTransition(c, corres.get(suc));
            }
        }
        return d;
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
        for (Etat e : auto) {
            for (char c : auto.alphabet()) {
                if (!e.transitions.keySet().contains(c)) {
                    e.ajouteTransition(c, pb);
                }
            }
        }
        return auto;
    }

    public Automate miroir() {

        Automate auto = new Automate();
        HashSet<Etat> set = new HashSet<Etat>();
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

    public Automate complementaire() {
        Automate auto = this.copie().complete().determinise();
        for (Etat e : auto) {
            e.setTerm(!e.isTerm());
        }
        return auto;
    }

    public Automate union(Automate a) {
        Automate cp = this.copie().determinise();
        Automate cp2 = a.copie().determinise();

        Automate union = cp;
        for (Etat e : cp2.initiaux) {
            for (Etat ee : e.succ()) {
                union.initiaux.add(e);
            }
        }

        return union.determinise();
    }

    public Automate intersection(Automate a) {
        return this.complementaire().union(a.complementaire()).complementaire();
    }

    public static Automate arbreToAutomate(Arbre a) {
        Automate auto = new Automate();
        Etat initial = new Etat(true, false, 0);
        int id = 1;
        HashMap<Feuille, Etat> map = new HashMap<>();
        for (Feuille f : a.succ().keySet()) {
            map.put(f, new Etat(false, a.derniers.contains(f), id));
            id++;
        }
        System.out.println(map);
        for (Feuille f : a.premiers) {
            initial.ajouteTransition(f.symbole, map.get(f));
        }
        System.out.println(initial.toString());
        for (Feuille f : map.keySet()) {
            for (Feuille f2 : a.succ().keySet()) {
                if (a.succ().get(f).contains(f2)) {
                    map.get(f).ajouteTransition(f2.symbole, map.get(f2));
                }
            }
        }
        auto.ajouteEtatRecursif(initial);
        return auto;
    }

    public void toFile(String path) {
        File f = new File(path);
        try {
            PrintStream ps = new PrintStream(f);
            ps.print(this.toString());
            ps.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erreur ecriture fichier");
        }
    }
}
