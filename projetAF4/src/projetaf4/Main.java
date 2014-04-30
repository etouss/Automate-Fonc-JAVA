package projetaf4;
public class Main {
    //Prochain a faire : alphabet()

    public static void main(String[] args){
        
        Automate af = new Automate("./test.txt");
/*
        Automate auto = new Automate();
        Etat a = new Etat(true,false,1);//Initiale

        Etat b = new Etat(false,false,2);
        Etat c = new Etat(false,false,3);
        Etat d = new Etat(false,false,4);
        Etat e = new Etat(false,false,5);
        Etat f = new Etat(false,false,6);

        Etat g = new Etat(false,true,7);//Terminale

        a.ajouteTransition('b', b);
        a.ajouteTransition('c', c);
        b.ajouteTransition('d', d);
        c.ajouteTransition('e', e);
        d.ajouteTransition('f', f);
        e.ajouteTransition('f', f);
        f.ajouteTransition('g', g);

        auto.ajouteEtatRecursif(a);
        //System.out.println(auto.estDeterministe());
        //a.ajouteTransition('b', c);
        //System.out.println(auto.estDeterministe());
        //auto = auto.determinise();
        System.out.println(auto.toString()+" \nFini");

        System.out.println(auto.miroir().toString()+" \nFini");
        //System.out.println(auto.estDeterministe());*/

        /*Feuille a = new Feuille('a');
        Feuille b = new Feuille('b');
        Feuille d = new Feuille('d');
        Binaire plusg = new Binaire(a,b,'+');
        Feuille c = new Feuille('c');
        Binaire foisd = new Binaire(d,c,'.');
        Unaire etoile = new Unaire(plusg,'*');
        Binaire foisc = new Binaire(foisd, etoile,'.');
        //Unaire etoile2 = new Unaire(foisd,'*');
        //Binaire foisc = new Binaire(etoile,etoile2,'.');
        System.out.println(foisc);
        System.out.println(etoile.contientMotVide);
        for (Feuille f : foisc.premiers){
            System.out.println(f.symbole);
        }
        System.out.println(foisc.succ().toString());
        
        Automate auto = Automate.arbreToAutomate(foisc);
        
        System.out.println("-----------------");
        System.out.print(auto.toString());
        //auto.toFile("./test.txt");*/
    }
}
