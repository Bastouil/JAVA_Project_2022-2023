import java.awt.*;
import java.awt.event.*;
import java.awt.Color.*;
import java.util.*;

import java.lang.Math;
import javax.swing.*;

public class MapInterface extends JFrame
{
	// Attributs **********************************************************************************
	private Random rand;
	
	protected float maxHeight = 100;
	
	protected JButton[][] caseButtons;
	protected int [][] intButtons;
	protected JPanel panel;
	
	protected final int sizeButton = 50;
	protected final int spaceBetweenButton = 1;
	
	// Constructeurs ******************************************************************************
	
	MapInterface(int mapHeight, int mapWidth)
	{
		super("Recherche de chemin !");
		this.setSize(1200,800);	
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0 , 0 , 1200 , 800);
		panel.setBackground(Color.gray);
		
		this.add(panel);
		
		rand = new Random();
		
		caseButtons = new JButton [mapHeight][mapWidth];
		intButtons = new int [mapHeight][mapWidth];
		for(int j= 0; j < mapWidth; j++){
		for(int i= 0; i < mapHeight; i++){
			caseButtons[i][j] = new JButton();
			//intButtons[i][j] = rand.nextInt(32);
			intButtons[i][j] = i*mapHeight + j;
			//UpdateMaxHeight(intButtons[i][j]);
			caseButtons[i][j].setText("" + intButtons[i][j]);
			caseButtons[i][j].setName(i+" "+j);  // mettre une info dans le bouton
			caseButtons[i][j].setBounds(1 + (j*(sizeButton + spaceBetweenButton)), 1 +(i*(sizeButton + spaceBetweenButton)), sizeButton , sizeButton);
			caseButtons[i][j].addMouseListener(new java.awt.event.MouseAdapter()
			{  
				public void mousePressed(java.awt.event.MouseEvent evt)
				{   
					JButton x = (JButton) evt.getSource();   /// recupere le bouton enfonce
					System.out.println("Nom :" + x.getName());  /// recupere l'info
				}
			});
			caseButtons[i][j].setBackground(getColor(intButtons[i][j]));
			panel.add(caseButtons[i][j]);
		}}
		
		this.setVisible(true);
	}

	// Methodes ***********************************************************************************
	
	private Color getColor(int x)
	{
		int R, G, B;
		double interSize = this.maxHeight/7;
		
		if(x <= interSize)
		{
			R = 0;
			G = 0;
			B = Math.round((7/this.maxHeight) * 255 * x);
		}
		else if(x <= 2*interSize)
		{
			R = 0;
			G = Math.round((7/this.maxHeight) * 255 * x - 255);
			B = 255;
		}
		else if(x <= 3*interSize)
		{
			R = 0;
			G = 255;
			B = Math.round(- ((7/this.maxHeight) * 255 * x - (3 * 255)));
		}
		else if(x <= 4*interSize)
		{
			R = Math.round((7/this.maxHeight) * 255 * x - (3 * 255));
			G = 255;
			B = 0;
		}
		else if(x <= 5*interSize)
		{
			R = 255;
			G = Math.round(- ((7/this.maxHeight) * 255 * x - (5 * 255)));
			B = 0;
		}
		else if(x <= 6*interSize)
		{
			R = 255;
			G = 0;
			B = Math.round((7/this.maxHeight) * 255 * x - (5 * 255));
		}
		else
		{
			R = 255;
			G = Math.round((7/this.maxHeight) * 255 * x - (6 * 255));
			B = 255;
		}
	
		System.out.println("\nx = " + x + "\nR = " + R + "\tG = " + G + "\tB = " + B);
		Color caseColor = new Color(R, G, B);
		return(caseColor);
	}
	
	private void UpdateMaxHeight(int x)
	{
		if(this.maxHeight < x){ this.maxHeight = x; }
	}
	
}
