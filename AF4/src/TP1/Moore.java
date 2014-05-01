package TP1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Moore {
	
	private Character[] lettresList;
	private Etat[] etatsList;
	private Integer[] etatsTypeList;
	private Integer[][] moore;
	
	public Automate miniMoore(Automate auto){
		return this.mooreMinimisation(auto.determinise()).suppressPoubelle();
	}
	
	
	void init(Automate auto){
		int nb = 0;
		HashSet<Character> lettres = new HashSet<Character>(); 
		for (Etat e : auto){
			lettres.addAll(e.transitions.keySet());
			nb++;
		}
		etatsList = new Etat[nb];
		lettresList = new Character[lettres.size()];
		for(int i=0;i<nb;i++)etatsList[i]=(Etat) auto.toArray()[i];
		for(int i=0;i<lettres.size();i++)lettresList[i] = (Character) lettres.toArray()[i];
		etatsTypeList = new Integer[nb];
		moore = new Integer[nb][lettresList.length];
		int i = 0;
		for(Etat e:etatsList){
			etatsTypeList[i++] = e.isTerm()?1:0;
		}
		
	}


	void recursif(){
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
		/*for(i=0;i<etatsTypeList.length;i++){
			System.out.print(etatsList[i].getId()+"|"+etatsTypeList[i]+"|");
			for(int j= 0;j<lettresList.length;j++){
				System.out.print(moore[i][j]+":");
			}
			System.out.println();
		}
		System.out.println("--------------------------");*/
		for(i=0;i<etatsTypeList.length;i++)test = test && etatsTypeList2[i]==etatsTypeList[i];
		if(!test){
			etatsTypeList = etatsTypeList2;
			this.recursif();
		}
		
	}
	
	public Automate mooreMinimisation(Automate auto){
		//System.out.print(auto.getInitiaux());
        init(auto);
        recursif();
        HashSet<Integer> set = new HashSet<Integer>();
        HashMap<Etat,Integer[]> map = new HashMap<Etat,Integer[]>();
        Automate a = new Automate();
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
        
        
        return a;
    }


}