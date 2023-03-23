import java.awt.*;
import java.awt.event.*;
import java.awt.Color.*;
import java.util.*;

import java.lang.Math;
import javax.swing.*;

/*
 * Les cases de la carte
 */
public class Land extends JButton {
	// Attributs *******************************************************************
	// attribus servants la coloration des boutons
	static final float maxHeight = 30;
	static final float interSize = maxHeight / 7;
	static int R, G, B;
	// position du bouton dans la carte
	private int Xcoord, Ycoord;
	// contient l'entier de la case
	public int cInt;

	// Constructeurs ***************************************************************
	/*
	 * Les paramètres du constructeur sont les coordonnées du bouton au sein de la
	 * carte
	 */
	Land(int x, int y) {
		super();
		// initialisation de la posistion du bouton dans la carte
		Xcoord = x;
		Ycoord = y;
		// On initialise l'entier de la case à 0
		cInt = 0;
		// le nombre que contient la case est affiché sur le bouton
		this.setText("" + cInt);
		// le nom du bouton est sa position dans la carte
		this.setName(x + ";" + y);
		// on met à jour la couleur du bouton (elle dépend de la valeur qu'il contient)
		UpdateLandColor();
		// on ajoute un listener sur le bouton
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				// recupere le bouton enfonce
				Land landSource = (Land) evt.getSource();
				// recupere l'info
				System.out.println("(" + landSource.getName() + ")");
				EditInterface editer = new EditInterface(landSource);
			}
		});
	}

	// Methodes ********************************************************************
	/*
	 * Renvoie une couleur en fonction d'un entier
	 */
	private Color getColor(int x) {
		if (x <= interSize) {
			R = 0;
			G = 0;
			B = Math.round((7 / maxHeight) * 255 * x);
		} else if (x <= 2 * interSize) {
			R = 0;
			G = Math.round((7 / maxHeight) * 255 * x - 255);
			B = 255;
		} else if (x <= 3 * interSize) {
			R = 0;
			G = 255;
			B = Math.round(-((7 / maxHeight) * 255 * x - (3 * 255)));
		} else if (x <= 4 * interSize) {
			R = Math.round((7 / maxHeight) * 255 * x - (3 * 255));
			G = 255;
			B = 0;
		} else if (x <= 5 * interSize) {
			R = 255;
			G = Math.round(-((7 / maxHeight) * 255 * x - (5 * 255)));
			B = 0;
		} else if (x <= 6 * interSize) {
			R = 255;
			G = 0;
			B = Math.round((7 / maxHeight) * 255 * x - (5 * 255));
		} else {
			R = 255;
			G = Math.round((7 / maxHeight) * 255 * x - (6 * 255));
			B = 255;
		}
		Color lColor = new Color(R, G, B);
		return lColor;
	}

	/*
	 * Renvoie la coordonnée demandée
	 */
	public int getXcoord() {
		return Xcoord;
	}

	public int getYcoord() {
		return Ycoord;
	}

	/*
	 * Met à jour la couleur du bouton à partir de l'entier
	 */
	public void UpdateLandColor() {
		setText("" + cInt);
		setBackground(getColor(cInt));
	}

}
