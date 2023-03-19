import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
/*
 * Interface de modification pour une case de la carte
 */
public class EditLandInterface extends JDialog
{
	// Attributs **********************************************************************************
	// code erreure
	protected final static int NOT_A_INT = -1;
	// dimensions de la fenêtre de dialogue
	private final static int wightFrame = 200;
	private final static int heightFrame = 125;
	// label qui va contenir le titre
	protected final static JLabel title = new JLabel("Entrez un entier entre 0 et 30 :");;
	// zone de texte où va écrire l'utilisateur
	protected JTextField textField;
	// bouton de confirmation (est pressé lorsque l'utilisateur à finit d'écrire l'entier)
	protected JButton okButton;
	
	// Constructeurs ******************************************************************************
	EditLandInterface(Land landSource)
	{
		// paramètrage de l'interface
		super();
		setSize(wightFrame , heightFrame);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// instanciation de la zone de texte
		textField = new JTextField();
		textField.addKeyListener(new KeyListener()
		{
		    public void keyPressed(KeyEvent e)
		    {
		    	if (e.getKeyCode() == KeyEvent.VK_ENTER)
		    	{
		    		ChangeLandNumber(landSource);
		        }
		    }
		    
		    public void keyReleased(KeyEvent e) {}
		    public void keyTyped(KeyEvent e) {}
		});
		// instanciation et paramètrage du bouton
		okButton = new JButton();
		okButton.setText("OK");
		okButton.addMouseListener(new MouseAdapter()
		{  
			public void mousePressed(MouseEvent evt)
			{   
				ChangeLandNumber(landSource);
			}
		});
		// ajout du titre, de la zone de texte et du bouton à l'interface
		getContentPane().add(title, BorderLayout.NORTH);
		getContentPane().add(textField, BorderLayout.CENTER);
		getContentPane().add(okButton, BorderLayout.SOUTH);
		
		setVisible(true);		
		
	}
	// Methodes ***********************************************************************************
	// Récupère l'entier contenu dans la zone de texte
	private int getInt()
	{
		// contenu brut de la zone de texte
		String textContent = textField.getText();
		try
		{
		    int num = Integer.parseInt(textContent);
		    
		    if(num < 0)
		    {
		    	num = 0;
		    }
		    else if(num > 30)
		    {
		    	num = 30;
		    }
		    
		    return num;
		}
		catch (NumberFormatException e)
		{
		    System.out.println("La chaine de caractere n'est pas un entier");
		    return(NOT_A_INT);
		}
	}
	
	// Execute l'action du bouton OK et de la touche entrer
	protected void ChangeLandNumber(Land landSource)
	{
		int newInt = getInt();
		if (newInt != NOT_A_INT)
		{
			landSource.setText("" + newInt);
			landSource.cInt = newInt;
			landSource.UpdateLandColor();
			dispose();
		}
	}
}
