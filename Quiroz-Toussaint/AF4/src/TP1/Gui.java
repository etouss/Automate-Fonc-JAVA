package TP1;

import java.util.Date;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Gui extends JPanel {
	private JButton importerAutomate = new JButton("Import Automate");
	private JButton importerExpression = new JButton("Import Expression");
	private JButton minimiserMoore = new JButton("Minimiser Automate (Moore)");
	private JButton minimiserResiduel = new JButton("Minimiser Expression (Residuel)");
	private JTextField expression = new JTextField();
	private JTextArea resultat = new JTextArea();
	
	private JButton generer = new JButton("Generer");
	private JTextField nbEtat = new JTextField();
	private JTextField alphabet = new JTextField();
	
	private JButton sauver = new JButton("Sauvegarder");
	private JButton determinise = new JButton("Determinise");
	
	private Automate automate;
	private Arbre arbre;
	
	public Gui(){
		setLayout(null);
		expression.setText("");
		BoutonListener boutonListener = new BoutonListener();
		importerAutomate.addActionListener(boutonListener);
		importerExpression.addActionListener(boutonListener);
		minimiserMoore.addActionListener(boutonListener);
		minimiserResiduel.addActionListener(boutonListener);
		generer.addActionListener(boutonListener);
		sauver.addActionListener(boutonListener);
		determinise.addActionListener(boutonListener);
		
		resultat.setSize(400, 600);
		resultat.setMargin(new Insets(10,10,10,10) );
		resultat.setEditable(false);
		JScrollPane scrollPane =  new JScrollPane(resultat,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(210, 10, 400, 600);
		
		add(scrollPane, null);
		add(importerAutomate);
        add(importerExpression);
        add(minimiserMoore);
        add(minimiserResiduel);
        add(expression);
        add(generer);
        add(nbEtat);
        add(alphabet);
        add(sauver);
        add(determinise);
        
        alphabet.setText("alphabet");
        nbEtat.setText("Nombre Etats");
        
        importerAutomate.setBounds(0,10,200,50);
        importerExpression.setBounds(0, 70, 200, 50);
        expression.setBounds(0, 130, 200, 50);
        minimiserMoore.setBounds(0, 190, 200, 50);
        minimiserResiduel.setBounds(0, 250, 200, 50);
        determinise.setBounds(0,310, 200, 50);
        generer.setBounds(0, 370, 200, 50);
        nbEtat.setBounds(0,430,95,50);
        alphabet.setBounds(105,430,95,50);
        sauver.setBounds(0,520,200,50);
	}

	
	public Arbre getArbre() {
		return arbre;
	}


	public void setArbre(Arbre arbre) {
		this.arbre = arbre;
	}


	public Automate getAutomate() {
		return automate;
	}


	public void setAutomate(Automate automate) {
		this.automate = automate;
	}
	
	public void setResultat(String resultatString) {
		this.resultat.setText(resultatString);
	}
	
	public String getExpression() {
		return expression.getText();
	}


	class BoutonListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	String bouton = ((JButton)e.getSource()).getText();
	    	switch(bouton){
	    	//Ouvre un fichier contenant un automate.
	    	case"Import Automate":
	    		String path = this.locateFile();
	    		setAutomate(new Automate(path));
	    		setResultat(getAutomate().toString());
	    		break;
	    	//Creer un automate depuis l'expression inscrite dans le champs
	    	case"Import Expression":
	    		String exp = getExpression();
	    		setArbre(Arbre.lirePostfixe(exp));
	    		setAutomate(getArbre().toAutomate());
	    		setResultat(getAutomate().toString());
	    		break;
	    	//Minimise l'automate par moore
	    	case"Minimiser Automate (Moore)":
	    		Moore moore = new Moore();
	    		setAutomate(moore.miniMoore(getAutomate()));
	    		setResultat(getAutomate().toString());
	    		break;
	    	//Minimise un automate depuis l'expression
	    	case"Minimiser Expression (Residuel)":
	    		Residuel resi = new Residuel();
	    		setAutomate(resi.miniResiduel(getArbre()));
	    		setResultat(getAutomate().toString());
	    		break;
	    	//Generer un automate avec les donnee mis dans les champs
	    	case"Generer":
	    		Generateur gene = new Generateur(alphabet.getText(),Integer.decode(nbEtat.getText()));
	    		setAutomate(gene.generer());
	    		setResultat(getAutomate().toString());
	    		break;
	    	//Sauvegarde l'automate dans le dossier choisi.
	    	case"Sauvegarder":
	    		String pathS = this.locateDossier();
	    		Date date = new Date();
	    		if(getExpression().equals(""))pathS=pathS.concat("/").concat(String.valueOf(date.getTime())).concat(".automate");
	    		else pathS=pathS.concat("/").concat(getExpression()).concat(".automate");
	    		getAutomate().toFile(pathS);
	    		//System.out.println(pathS);
	    		break;
	    	//Determinise l'automate
	    	case"Determinise":
	    		setAutomate(getAutomate().determinise());
	    		setResultat(getAutomate().toString());
	    		break;
	    	}
	    	
	    
	    } 
	    //Ouvre un explorateur de fichier.
	    public String locateFile(){
	    	JFrame popupMenu = null;
    		final JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(popupMenu);
            return fc.getSelectedFile().getAbsolutePath();
	    }
	    public String locateDossier(){
	    	JFrame popupMenu = null;
    		final JFileChooser fc = new JFileChooser();
    		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.showOpenDialog(popupMenu);
            File selectedDir = fc.getSelectedFile();
            if ( !selectedDir.isDirectory() ) {
        		selectedDir = selectedDir.getParentFile();
        	}
            //System.out.println(selectedDir.getAbsolutePath());
            return selectedDir.getAbsolutePath();
	    }
	    
	 }
}




