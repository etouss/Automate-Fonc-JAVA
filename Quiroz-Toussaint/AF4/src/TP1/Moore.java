package TP1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Moore {
	
	private Character[] lettresList;
	private Etat[] etatsList;
	private Integer[] etatsTypeList;
	private Integer[][] moore;
	
	/** Minimise l'automate par la methode de moore le determinise et supprime la poubelle.
     * 
     * @param auto 
     * 			L'automate a minimiser
     * @return 
     * 			L'automate minimiser
     */
	public Automate miniMoore(Automate auto){
		return this.mooreMinimisation(auto.determinise()).suppressPoubelle();
	}
	
	/** Initialise Moore avec un automate
     * 
     * @param auto 
     * 			L'automate a minimiser
     */
	void init(Automate auto){
		int nb = 0;
		//Recupere tout l'alphabet de l'automate
		HashSet<Character> lettres = new HashSet<Character>(); 
		for (Etat e : auto){
			lettres.addAll(e.transitions.keySet());
			nb++;
		}
		//Creer un tableau avec les etat de facon a conserver l'ordre.
		etatsList = new Etat[nb];
		//Creer un tableau avec l'aplphabet de facon a conserver l'ordre.
		lettresList = new Character[lettres.size()];
		for(int i=0;i<nb;i++)etatsList[i]=(Etat) auto.toArray()[i];
		for(int i=0;i<lettres.size();i++)lettresList[i] = (Character) lettres.toArray()[i];
		etatsTypeList = new Integer[nb];
		moore = new Integer[nb][lettresList.length];
		int i = 0;
		//initialise Etats type liste selon si terminal ou non.
		for(Etat e:etatsList){
			etatsTypeList[i++] = e.isTerm()?1:0;
		}
		
	}

	/** Calcul le tableau moore corespondant au transitions et le tableau etatTypeList correspondant au nouveau etat.
     */
	void recursif(){
		//Calcule moore[] pour chacun des etats.
		int i = 0;
		for (Etat e : etatsList){
			int j=0;
			Integer[] mooreEtat = new Integer[lettresList.length];
			for(Character l : lettresList){
				if(e.transitions.get(l) == null ||e.transitions.get(l).size()==0){
					mooreEtat[j]=-1;
				}
				else{
					Etat succ = new Etat();
					for (Etat e2 : e.transitions.get(l)) succ = e2;
					int t=0;
					for (Etat e2 : etatsList){
						if(e2==succ){
							mooreEtat[j]=etatsTypeList[t];
						}
						t++;	
					}
				}
				j++;
			}
			moore[i]=mooreEtat;
			i++;	
		}
		//Determine EtatTypeList par rapport a moore et EtatTypeList precedant.
		i = 0;
		Integer[] etatsTypeList2 = new Integer[etatsTypeList.length];
		for(int e2 = 0;e2<etatsTypeList.length;e2++)etatsTypeList2[e2]=-1;
		int k = 0;
		//etatsTypeList2[0]=k++;
		for(Integer type : etatsTypeList){
			if(etatsTypeList2[i]==-1){
				etatsTypeList2[i]=k++;
				for (int j=i+1;j<etatsTypeList.length;j++){
					if(type==etatsTypeList[j]){
						boolean test = true;
						for(int l = 0;l<lettresList.length;l++){
							test = test && (moore[i][l]==moore[j][l]);
						}
						if(test){
							etatsTypeList2[j]=etatsTypeList2[i];
						}
						
					}
				}
			}
			i++;
		}
		
		boolean test = true;
		//verifie si le nouvelle etattypelist et pareil ou precedant si oui termine sinon declenche la recursivite.
		for(i=0;i<etatsTypeList.length;i++)test = test && etatsTypeList2[i]==etatsTypeList[i];
		if(!test){
			etatsTypeList = etatsTypeList2;
			this.recursif();
		}
		
	}
	/** Minimise l'automate par la methode de moore
     * 
     * @param auto 
     * 			L'automate a minimiser
     * @return 
     * 			L'automate minimiser avec poubelle.
     */
	public Automate mooreMinimisation(Automate auto){
		//System.out.print(auto.getInitiaux());
        init(auto);
        recursif();
        HashSet<Integer> set = new HashSet<Integer>();
        HashMap<Etat,Integer[]> map = new HashMap<Etat,Integer[]>();
        Automate a = new Automate();
        //Creer les etat et les transistion grace a EtatType creer et Moore[][]
        //Creer les etats.
        for(int i = 0;i < etatsTypeList.length;i++){
            if(!set.contains(etatsTypeList[i])){
            	//System.out.println(etatsList[i]);
                set.add(etatsTypeList[i]);
                boolean init = false;
                for(int k=0;k<etatsTypeList.length;k++){
                	if(etatsTypeList[k]==etatsTypeList[i]){
                		init = etatsList[k].isInit()||init;
                	}
                }
                Etat e = new Etat(init,etatsList[i].isTerm(),etatsTypeList[i]);
                map.put(e,moore[i]);
            }
        }
        //Ajoute les transistions.
        for(Etat e : map.keySet()){
            for (int i = 0; i<lettresList.length;i++){
                if(map.get(e)[i]!=-1){
                    Etat tran = new Etat();
                    for(Etat e2: map.keySet()){
                        if(e2.id == map.get(e)[i])tran=e2;
                    }
                    e.ajouteTransition(lettresList[i],tran);
                }
            }
        }
        for(Etat e : map.keySet()){
            a.ajouteEtatRecursif(e);
        }
        
        //retourne l'automate.
        return a;
    }


}