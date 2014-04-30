package projetaf4;

import java.util.HashMap;
import java.util.Set;

public class Etat {

    HashMap<Character, EnsEtat> transitions;
    boolean init;
    boolean term;
    int id;

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

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return this.id;
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

    public EnsEtat succ(char c) {
        return transitions.get(c);
    }

    public EnsEtat succ() {

        EnsEtat l = new EnsEtat();
        for (char c : transitions.keySet()) {
            l.addAll(this.succ(c));
        }
        return l;
    }

    void ajouteTransition(char c, Etat e) {
        if (transitions.containsKey(c)) {
            transitions.get(c).add(e);
        } else {
            EnsEtat ee = new EnsEtat();
            ee.add(e);
            transitions.put(c, ee);
        }
    }

    public String toString1() {
        String s = "initial : " + this.isInit() + "\n";
        s += "terminal : " + this.isTerm() + "\n";
        s += "id : " + this.id + "\n";
        for (char c : transitions.keySet()) {
            for (Etat e : transitions.get(c)) {
                s += c + " : " + e.id + "\n";
            }
        }
        return s;
    }

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

    public Set<Character> alphabet() {
        return this.transitions.keySet();
    }

    public Etat copie() {
        return new Etat(this.init, this.term, this.id);
    }

}
