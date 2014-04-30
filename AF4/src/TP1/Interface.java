package TP1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import TP1.Gui.BoutonListener;

public class Interface extends JFrame{
	private JPanel container = new JPanel();;
	private Gui droit = new Gui();
	private Gui gauche = new Gui();
	private JButton egal = new JButton("Egal ?");
	private JLabel result = new JLabel();
	private JButton egalMini = new JButton("Egal Mini (Moore/Residuel) ?");
	
	public Interface(){
		this.setSize(1250, 750);
		this.setTitle("Projet AF4");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		
		BoutonListener boutonListener = new BoutonListener();
		egal.addActionListener(boutonListener);
		egalMini.addActionListener(boutonListener);
		
		this.container.setLayout(null);
		this.container.add(this.droit);
		this.container.add(this.gauche);
		this.container.add(this.egal);
		this.container.add(this.egalMini);
		this.container.add(this.result);
		this.gauche.setBounds(0,0,610,600);
		this.droit.setBounds(620,0,610,600);
		this.egal.setBounds(400, 620, 200, 50);
		this.egalMini.setBounds(400, 680, 200, 50);
		this.result.setBounds(610, 650, 200, 50);
		
		setContentPane(container);
		validate();
	}
	
	void setResult(String string){
		this.result.setText(string);
	}
	
	class BoutonListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	String bouton = ((JButton)e.getSource()).getText();
	    	Automate auto1 = gauche.getAutomate();
	    	Automate auto2 = droit.getAutomate();
	    	switch(bouton){
	    	case"Egal ?":
		    	if(auto1.egaliteAuto(auto2))setResult("Vrai");
		    	else setResult("Faux");
	    	break;
	    	case"Egal Mini (Moore/Residuel) ?":
		    	if(auto1.egaliteMini(auto2))setResult("Vrai");
		    	else setResult("Faux");
	    	break;
	    	}
	    }
	}
}
