package projetaf4;

import java.util.HashSet;
import java.util.Set;

public class EnsEtat extends HashSet<Etat> {

    public EnsEtat() {
    }

    public String toString() {
        String s = "";
        for (Etat e : this) {
            s += e.getId();
        }
        return s;
    }

    public EnsEtat succ(char c) {
        EnsEtat r = new EnsEtat();
        for (Etat e : this) {
            if(e.succ(c) != null){
                for (Etat ee : e.succ(c)) {
                    r.add(ee);
                }
            }
        }
        return r;
    }

    public EnsEtat succ() {
        EnsEtat r = new EnsEtat();
        for (Etat e : this) {
            for (Etat ee : e.succ()) {
                r.add(ee);
            }
        }
        return r;
    }
    public boolean contientTerminal(){
        for (Etat e : this){
            if(e.isTerm()) return true;
        }
       return false;
    }

     public Set<Character> alphabet(){
         HashSet<Character> set = new HashSet<Character>();
         for(Etat e : this){
             set.addAll(e.alphabet());
         }
         return set;
     }

}
