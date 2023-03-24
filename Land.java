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
	// comosante de la couleur du fond de la case
	int R, G, B;
	// boolean permettant de savoir la nature de la case
	boolean isStart;
	boolean isEnd;
	// carte dans lequel se trouve le bouton
	MapInterface map;
	// position du bouton dans la carte
	private int Xcoord, Ycoord;
	// contient l'entier de la case
	public int cInt;
	// les différents listeners d'une case
	final static MouseAdapter editLandListener = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			// recupere le bouton enfonce
			Land landSource = (Land) evt.getSource();
			System.out.println("Case : (" + landSource.getName() + ") en cours de modification");
			EditInterface editer = new EditInterface(landSource);
		}
	};
	final static MouseAdapter startLandListener = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			// recupere le bouton enfonce
			Land landSource = (Land) evt.getSource();
			landSource.nowStart();
			landSource.UpdateText();
			System.out.println("Case : (" + landSource.getName() + ") define comme depart");
			landSource.map.DefineLandsListener(MapInterface.EDIT_ACTION);
			landSource.map.DefaultMessage();
		}
	};
	final static MouseAdapter endLandListener = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			// recupere le bouton enfonce
			Land landSource = (Land) evt.getSource();
			landSource.nowEnd();
			landSource.UpdateText();
			System.out.println("Case : (" + landSource.getName() + ") define comme arrivee");
			landSource.map.DefineLandsListener(MapInterface.EDIT_ACTION);
			landSource.map.DefaultMessage();
		}
	};

	// Constructeurs ***************************************************************
	/*
	 * Les paramètres du constructeur sont les coordonnées du bouton au sein de la
	 * carte
	 */
	Land(MapInterface map, int x, int y) {
		super();
		// initialisation de la nature de la case
		isStart = false;
		isEnd = false;
		// récupération de la carte
		this.map = map;
		// récupération de la posistion du bouton dans la carte
		Xcoord = x;
		Ycoord = y;
		// On initialise l'entier de la case à 0
		cInt = 0;
		// le nombre que contient la case est affiché sur le bouton
		this.setText("" + cInt);
		// le nom du bouton est sa position dans la carte
		this.setName(x + ";" + y);
		// on met à jour la couleur du bouton (elle dépend de la valeur qu'il contient)
		UpdateColor();

	}

	// Methodes ********************************************************************
	/*
	 * Définit la case en tant que case de départ
	 */
	public void nowStart() {
		map.startLand.isStart = false;
		map.startLand.UpdateText();
		map.startLand = this;
		isStart = true;
		UpdateText();
	}
	/*
	 * Définit la case en tant que case d'arrivée
	 */
	public void nowEnd() {
		map.endLand.isEnd = false;
		map.endLand.UpdateText();
		map.endLand = this;
		isEnd = true;
	}
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
		return new Color(R, G, B);
	}

	/*
	 * Renvoie la couleur opposée à celle du fond. Il faut que les composantes de
	 * couleurs soient à jour pour que la couleur renvoyée soit bonne.
	 */
	private Color getOpositeColor() {
		R = 255 - R;
		G = 255 - G;
		B = 255 - B;

		return new Color(R, G, B);
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
	 * Met à jour la couleur du bouton (avec la couleur de l'écriture)
	 */
	public void UpdateColor() {
		// ordre très important lors de l'utilisation de ces deux fonctions
		setBackground(getColor(cInt));
		setForeground(getOpositeColor());
	}
	/*
	 * Met à jour le text affiché par le bouton
	 */
	public void UpdateText() {
		if (isStart && isEnd) {
			setText("D " + cInt + " A");
		}
		else if (isStart) {
			setText("D " + cInt);
		}
		else if (isEnd){
			setText(cInt + " A");
		}
		else {
			setText("" + cInt);
		}
	}
	/*
	 * Met à jour l'apparance du bouton à partir de sa nature et de l'entier (cInt)
	 */
	public void UpdateAppearance() {
		UpdateText();
		UpdateColor();
	}

}
