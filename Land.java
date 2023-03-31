/*
 * INF 1401 : projet JAVA 2022-2023
 * Trouver le meilleur chemin au sein d'une grille de case avec des nombres.
 * JAOUANNE Lilian & GARCON Bastian
 */

import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import javax.swing.*;

/*
 * Les cases de la carte
 */
public class Land extends JButton {
	// Attributs *******************************************************************
	public static final int OUT = -1;
	// attribus servants la coloration des boutons
	static final float maxHeight = 30;
	static final float interSize = maxHeight / 7;
	// comosante de la couleur du fond de la case
	int R, G, B;
	// boolean permettant de savoir la nature de la case (départ ou arrivée)
	boolean isStart;
	boolean isEnd;
	// boolean permettant de savoir si la case ne fait pas partie du chemin
	boolean isFree;
	// boolean permettant de savoir si la case est dans la carte
	final boolean inMap;
	// carte dans lequel se trouve le bouton
	MapInterface map;
	// position du bouton dans la carte
	private final int Xcoord, Ycoord;
	// contient l'entier de la case
	public int height;
	// les différents listeners d'une case
	final static MouseAdapter editLandListener = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			// recupere le bouton enfonce
			Land landSource = (Land) evt.getSource();
			if(!landSource.map.barMenu.displayHeightColor.isSelected()) {
				landSource.map.ColorMapHeight();
				landSource.map.setAllLandFree();
			}
			System.out.println("Case : " + landSource.getName() + " en cours de modification");
			landSource.map.message.setText("Case " + landSource.getName() + " en cours de modification");
			EditInterface editer = new EditInterface(landSource);
		}
	};
	final static MouseAdapter startLandListener = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			// recupere le bouton enfonce
			Land landSource = (Land) evt.getSource();
			landSource.nowStart();
			landSource.UpdateText();
			System.out.println("Case : " + landSource.getName() + " define comme depart");
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
			System.out.println("Case : " + landSource.getName() + " define comme arrivee");
			landSource.map.DefineLandsListener(MapInterface.EDIT_ACTION);
			landSource.map.DefaultMessage();
		}
	};

	// Constructeurs ***************************************************************
	/*
	 * Constructeur d'une case ce trouvant dans la carte
	 */
	Land(MapInterface map, int x, int y) {
		super();
		// initialisation de la nature de la case
		isStart = false;
		isEnd = false;
		isFree = true;
		inMap = true;
		// récupération de la carte
		this.map = map;
		// récupération de la posistion du bouton dans la carte
		Xcoord = x - 2;
		Ycoord = y - 2;
		// On initialise l'entier de la case à 0
		height = 0;
		// le nombre que contient la case est affiché sur le bouton
		this.setText("" + height);
		// le nom du bouton est sa position dans la carte
		this.setName("(" + Xcoord + " ; " + Ycoord + ")");
		// on met à jour la couleur du bouton (elle dépend de la valeur qu'il contient)
		UpdateColor();
	}
	/*
	 * Constructeur d'une case ce trouvant en dehors de la carte
	 */
	Land(int x, int y) {
		super();
		isStart = false;
		isEnd = false;
		isFree = false;
		inMap = false;
		this.map = null;
		Xcoord = x - 2;
		Ycoord = y - 2;
		// attribution d'une hauteur non valide : OUT = -1
		height = OUT;
		this.setName("OUT");
	}

	// Methodes ********************************************************************
	/*
	 * Dit si on peut passer à la case suivante en fonction de la différence de
	 * hauteur
	 */
	public boolean IsPassableTo(Land nextLand)
	{
		int heightDifference = height - nextLand.height;
		if(-3 <= heightDifference && heightDifference <= 3) {
			return true;
		}
		return false;
	}
	/*
	 * Renvoie le cout du chemin en passant de la case "this." à la case "nextLand"
	 */
	public int CostTo(Land nextLand)
	{
		if (this == nextLand) {
			System.out.println("Attention calcul de cout avec deux cases identiques");
		}
		
		try {
			int heightDifference = nextLand.height - height;
			// ça descent ou c'est plat donc le coût est nul
			if (heightDifference <= 0)
				return 0;
			//ça monte donc on renvoie la différence
			else
				return heightDifference;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return 0;
		}
	}
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
		UpdateText();
	}
	/*
	 * Color la case d'une couleur donnée et color le texte de la case de la couleur
	 * opposée à celle donnée
	 */
	protected void ColorWith(Color x) {
		setBackground(x);
		R = x.getRed();
		G = x.getGreen();
		B = x.getBlue();
		setForeground(getColorOpositeBackground());
	}
	/*
	 * Renvoie une couleur en fonction d'un entier
	 */
	private Color getColor() {
		if (height <= interSize) {
			R = 0;
			G = 0;
			B = Math.round((7 / maxHeight) * 255 * height);
		} else if (height <= 2 * interSize) {
			R = 0;
			G = Math.round((7 / maxHeight) * 255 * height - 255);
			B = 255;
		} else if (height <= 3 * interSize) {
			R = 0;
			G = 255;
			B = Math.round(-((7 / maxHeight) * 255 * height - (3 * 255)));
		} else if (height <= 4 * interSize) {
			R = Math.round((7 / maxHeight) * 255 * height - (3 * 255));
			G = 255;
			B = 0;
		} else if (height <= 5 * interSize) {
			R = 255;
			G = Math.round(-((7 / maxHeight) * 255 * height - (5 * 255)));
			B = 0;
		} else if (height <= 6 * interSize) {
			R = 255;
			G = 0;
			B = Math.round((7 / maxHeight) * 255 * height - (5 * 255));
		} else {
			R = 255;
			G = Math.round((7 / maxHeight) * 255 * height - (6 * 255));
			B = 255;
		}
		return new Color(R, G, B);
	}
	/*
	 * Renvoie la couleur opposée à celle du fond. Il faut que les composantes de
	 * couleurs soient à jour pour que la couleur renvoyée soit bonne.
	 */
	private Color getColorOpositeBackground() {
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
		setBackground(getColor());
		setForeground(getColorOpositeBackground());
	}
	/*
	 * Met à jour le text affiché par le bouton
	 */
	public void UpdateText() {
		if (isStart && isEnd) {
			setText("D " + height + " A");
		}
		else if (isStart) {
			setText("D " + height);
		}
		else if (isEnd){
			setText(height + " A");
		}
		else {
			setText("" + height);
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
