import java.awt.*;
import java.awt.event.*;
import java.awt.Color.*;
import java.util.*;

import java.lang.Math;
import javax.swing.*;
/*
 * Interface de la carte.
 */
public class MapInterface extends JFrame
{
	// Attributs **********************************************************************************
	protected BarMenu barMenu;
	protected Land [][] Map;
	protected JPanel frame;
	static final int sizeFrame = 600;
	
	// Constructeurs ******************************************************************************
	MapInterface(int nbRow, int nbCols)
	{
		// paramètrage de l'interface
		super("Interface principale");
		this.setSize(sizeFrame,sizeFrame);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// allocation du panneau
		frame = new JPanel(new GridLayout(nbRow, nbCols));
		
		// allocation de la barre de menu
		barMenu = new BarMenu();
		this.setJMenuBar(barMenu);
		// allocation du tableau de bouton/entier
		Map = new Land [nbRow][nbCols];
		for(int j= 0; j < nbCols; j++)
		{
			for(int i= 0; i < nbRow; i++)
			{
				// allocation d'un bouton/entier
				Map[i][j] = new Land(i, j);
				// ajout de du bouton au panneau
				frame.add(Map[i][j]);
			}
		}
		
		// ajout du panneau et de la barre de menu à l'interface
		this.getContentPane().add(frame, BorderLayout.CENTER);
		this.getContentPane().add(barMenu, BorderLayout.NORTH);
		this.setVisible(true);
	}

	// Methodes ***********************************************************************************
	// met à jour la couleur de la carte
	protected void UpdateMapColor()
	{
		for(int j= 0; j < Map[0].length; j++)
		{
			for(int i= 0; i < Map.length; i++)
			{
				Map[i][j].UpdateLandColor();
			}
		}
	}
}
