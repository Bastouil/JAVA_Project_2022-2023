import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Color.*;
import java.util.*;

import java.lang.Math;
import javax.swing.*;

/*
 * Interface de la carte.
 */
public class MapInterface extends JFrame {
	// Attributs *******************************************************************
	// codes utilis�es pour saisir les diff�rents listeners
	public static final int EDIT_ACTION = 1;
	public static final int START_ACTION = 2;
	public static final int END_ACTION = 3;
	// taille des c�t�es de la fen�tre
	private static final int SIZE_FRAME = 600;
	// taille de la carte
	protected int nbRows;
	protected int nbCols;
	// barre de menu
	protected BarMenu barMenu;
	// tableau de case
	protected Land[][] land;
	// case de d�part
	protected Land startLand;
	// case d'arriv�e
	protected Land endLand;
	// grille dans laquelle on vient mettre les cases
	protected JPanel frame;
	// message qui s'affiche � l'utilisateur
	protected JLabel message;
	// pile des meilleurs choix pour le chemin de moindre co�t
	protected Stack<Situation> bestPath;

	// Constructeurs ***************************************************************
	MapInterface(int nbRows, int nbCols) {
		// param�trage de l'interface
		super("Interface principale");
		setSize(SIZE_FRAME, SIZE_FRAME);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// instanciation du message
		message = new JLabel("Cliquez sur une case pour la modifier");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		// on garde les dimensions de la carte
		this.nbRows = nbRows;
		this.nbCols = nbCols;

		// instanciation du panneau
		frame = new JPanel(new GridLayout(nbRows, nbCols));

		// instanciation du tableau de cases
		land = new Land[nbRows + 4][nbCols + 4];
		// instanciation des cases non visibles
		int i, j;
		for (i = 0; i < nbRows + 4; i++) {
			land[i][0] = new Land(i, 0);
			land[i][1] = new Land(i, 1);
			land[i][nbRows + 1] = new Land(i, nbRows + 1);
			land[i][nbRows + 2] = new Land(i, nbRows + 2);
		}
		for (j = 0; j < nbCols + 4; j++) {
			land[0][j] = new Land(0, j);
			land[1][j] = new Land(1, j);
			land[nbCols + 1][j] = new Land(nbCols + 1, j);
			land[nbCols + 2][j] = new Land(nbCols + 2, j);
		}
		// instanciation des cases visibles
		for (i = 0; i < nbRows; i++) {
			for (j = 0; j < nbCols; j++) {
				// instanciation d'un case
				land[i + 2][j + 2] = new Land(this, i + 2, j + 2);
				// ajout de du bouton au panneau
				frame.add(landAt(i, j));
				landAt(i, j).addMouseListener(Land.editLandListener);
			}
		}
		// d�part et arriv�e initialis�s en dehors de la carte
		startLand = landAt(-2, -2);
		endLand = landAt(-2, -2);
		// instanciation de la barre de menu
		barMenu = new BarMenu();
		setJMenuBar(barMenu);
		// ajout des listener sur les sous-menus
		barMenu.saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Enregistrer\"");
				message.setText("Enregistrement d'un fichier");
				EditLandInterface editer = new EditLandInterface(GetMapInterface());
				editer.AddListenerWriteFile();
			}
		});
		barMenu.loadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Charger un fichier\"");
				message.setText("Chargement d'un fichier");
				EditLandInterface editer = new EditLandInterface(GetMapInterface());
				editer.AddListenerReadFile();
			}
		});
		barMenu.selectStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Selectionner le depart\"");
				message.setText("Veuillez selectionner la case de d�part");
				// change les listeners ce trouvant sur les boutons
				DefineLandsListener(START_ACTION);
			}
		});
		barMenu.selectEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Selectionner l'arrivee\"");
				message.setText("Veuillez selectionner la case d'arriv�e");
				// change les listeners ce trouvant sur les boutons
				DefineLandsListener(END_ACTION);
			}
		});
		barMenu.launchResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Lancer la resolution\"");
				if (!startLand.inMap || !endLand.inMap) {
					System.out.println("Lancement de r�solution refus�e");
					MessageInterface m = new MessageInterface(
							"Veuillez s�lectionner un case de d�part et d'arriv�e avant de lancer la r�solution");
					return;
				}
				setAllLandFree();
				message.setText("R�solution en cours ...");
				LaunchResolution();
				MessageInterface m = new MessageInterface("R�solution termin�e !");
				DefaultMessage();
				System.out.println("Resolution terminee !");
				ColorMapPath();
			}
		});
		barMenu.displayHeightColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ColorMapHeight();
			}
		});
		barMenu.displayPathColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ColorMapPath();
			}
		});
		
		String titreAbout = "A propos de l'application";
		barMenu.aboutApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Aide \"A propos\"");
				message.setText("A propos de l'application");
				EditLandInterface editer = new EditLandInterface(GetMapInterface(), titreAbout);
				editer.AddListenerWriteFile();
			}
		});

		// ajout du panneau et de la barre de menu � l'interface
		getContentPane().add(frame, BorderLayout.CENTER);
		getContentPane().add(barMenu, BorderLayout.NORTH);
		getContentPane().add(message, BorderLayout.SOUTH);
		setVisible(true);
	}

	// Methodes ********************************************************************
	/*
	 * Renvoie la case � partir des coordonn�es visibles par l'utilisateur
	 */
	public Land landAt(int x, int y) {
		return land[x + 2][y + 2];
	}

	/*
	 * Lib�re toutes les cases de la carte
	 */
	public void setAllLandFree() {
		for (int i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbCols; j++) {
				landAt(i, j).isFree = true;
			}
		}
	}
	/*
	 * Liber toutes les cases qui n'appartient pas � la pile donn�e et occupe toutes
	 * les autres
	 */
	void SetFreeExcept(Stack<Situation> S) {
		int i;
		// on lib�re toutes les cases
		for (i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbRows; j++) {
				landAt(i, j).isFree = true;
			}
		}
		// et on occupe celles qui apparitennent � la pile donn�e
		for (i = 0; i < S.table.size(); i++) {
			S.table.elementAt(i).lastLand.isFree = false;
		}
	}
	/*
	 * Met le message par d�faut sur le JLabel
	 */
	public void DefaultMessage() {
		message.setText("Cliquez sur une case pour la modifier");
	}

	/*
	 * Met � jour la couleur de la carte selon la hauteur des cases
	 */
	protected void ColorMapHeight() {
		barMenu.displayHeightColor.setSelected(true);
		for (int i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbCols; j++) {
				landAt(i, j).UpdateAppearance();
			}
		}
	}

	/*
	 * Met � jour la couleur de la carte selon le chemin
	 */
	protected void ColorMapPath() {
		barMenu.displayPathColor.setSelected(true);
		for (int i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbCols; j++) {
				if (landAt(i, j).isFree)
					landAt(i, j).ColorWith(Color.WHITE);
				else
					landAt(i, j).ColorWith(Color.RED);
			}
		}
	}

	/*
	 * Color la carte d'une couleur donn�e
	 */
	protected void ColorMap(Color x) {
		for (int i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbCols; j++) {
				landAt(i, j).ColorWith(x);
			}
		}
	}

	/*
	 * R�cuperer le pointeur du la structure MapInterface
	 */
	private MapInterface GetMapInterface() {
		return this;
	}

	/*
	 * D�finit les listeners � mettre sur les boutons (objets de type Land)
	 */
	public void DefineLandsListener(int CdeListener) {
		if (CdeListener == EDIT_ACTION) {
			for (int i = 0; i < nbRows; i++) {
				for (int j = 0; j < nbCols; j++) {
					for (MouseListener oldListener : landAt(i, j).getMouseListeners()) {
						landAt(i, j).removeMouseListener(oldListener);
					}
					landAt(i, j).addMouseListener(Land.editLandListener);
				}
			}
		} else if (CdeListener == START_ACTION) {
			for (int i = 0; i < nbRows; i++) {
				for (int j = 0; j < nbCols; j++) {
					for (MouseListener oldListener : landAt(i, j).getMouseListeners()) {
						landAt(i, j).removeMouseListener(oldListener);
					}
					landAt(i, j).addMouseListener(Land.startLandListener);
				}
			}
		} else if (CdeListener == END_ACTION) {
			for (int i = 0; i < nbRows; i++) {
				for (int j = 0; j < nbCols; j++) {
					for (MouseListener oldListener : landAt(i, j).getMouseListeners()) {
						landAt(i, j).removeMouseListener(oldListener);
					}
					landAt(i, j).addMouseListener(Land.endLandListener);
				}
			}
		}
	}

	/*
	 * Renvoie le choix possible
	 */
	private int NextPossibleChoice(int posX, int posY, int lastChoice, Land actualLand) {
		if (lastChoice == 0) {
			if (landAt(posX - 1, posY).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY)))
				return 1; // en HAUT
			if (landAt(posX - 1, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY + 1)))
				return 2; // en HAUT-DROITE
			if (landAt(posX, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX, posY + 1)))
				return 3; // � DROITE
			if (landAt(posX + 1, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY + 1)))
				return 4; // en BAS � DROITE
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 5; // en BAS
			if (landAt(posX + 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY - 1)))
				return 6; // en BAS � GAUCHE
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 7; // � GAUCHE
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}
		if (lastChoice == 1) {
			if (landAt(posX - 1, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY + 1)))
				return 2; // en HAUT-DROITE
			if (landAt(posX, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX, posY + 1)))
				return 3; // � DROITE
			if (landAt(posX + 1, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY + 1)))
				return 4; // en BAS � DROITE
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 5; // en BAS
			if (landAt(posX + 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY - 1)))
				return 6; // en BAS � GAUCHE
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 7; // � GAUCHE
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}
		if (lastChoice == 2) {
			if (landAt(posX, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX, posY + 1)))
				return 3; // � DROITE
			if (landAt(posX + 1, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY + 1)))
				return 4; // en BAS � DROITE
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 5; // en BAS
			if (landAt(posX + 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY - 1)))
				return 6; // en BAS � GAUCHE
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 7; // � GAUCHE
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}
		if (lastChoice == 3) {
			if (landAt(posX + 1, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY + 1)))
				return 4; // en BAS � DROITE
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 5; // en BAS
			if (landAt(posX + 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY - 1)))
				return 6; // en BAS � GAUCHE
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 7; // � GAUCHE
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}
		if (lastChoice == 4) {
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 5; // en BAS
			if (landAt(posX + 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY - 1)))
				return 6; // en BAS � GAUCHE
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 7; // � GAUCHE
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}
		if (lastChoice == 5) {
			if (landAt(posX + 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY - 1)))
				return 6; // en BAS � GAUCHE
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 7; // � GAUCHE
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}
		if (lastChoice == 6) {
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 7; // � GAUCHE
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}
		if (lastChoice == 7) {
			if (landAt(posX - 1, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY - 1)))
				return 8; // en HAUT � GAUCHE
		}

		return -1;
	}
	/*
	 * Trouve un chemin jusqu'� l'arriv�e � partir de la situation donn�e
	 */
	private Stack<Situation> FindPathFrom(Situation situation) {
		Situation lastSituation = situation;
		Land pLand = lastSituation.lastLand;
		// coordonn�e de pLand
		int posX = pLand.getXcoord();
		int posY = pLand.getYcoord();
		int lastChoice = lastSituation.lastChoice;
		int newChoice;

		Stack<Situation> S;
		if (bestPath == null) {
			S = new Stack<Situation>();
		}
		else {
			// copie colle bestPath dans S mais s'arr�te avant d'arriver � "situation"
			S = bestPath.DuplicateStackSituationUntil(situation);
		}
		// lib�re toutes les cases sauf celle de la pile S
		SetFreeExcept(S);
		System.out.println("Start Finding path with pLand : " + pLand.getName());
		while (endLand.isFree) {
			// cherche le d�placement suivant
			newChoice = NextPossibleChoice(posX, posY, lastChoice, pLand);

			if (newChoice != -1) {
				// on empile le deplacement
				S.Push(new Situation(newChoice, pLand));
				System.out.println("PUSH pLand : " + pLand.getName());
				// on va sur la nouvelle case
				if (newChoice == 1) {// en HAUT
					posX--;
				}
				if (newChoice == 2) {// en HAUT-DROITE
					posX--;
					posY++;
				}
				if (newChoice == 3) {// � DROITE
					posY++;
				}
				if (newChoice == 4) {// en BAS � DROITE
					posX++;
					posY++;
				}
				if (newChoice == 5) {// en BAS
					posX++;
				}
				if (newChoice == 6) {// en BAS � GAUCHE
					posX++;
					posY--;
				}
				if (newChoice == 7) {// � GAUCHE
					posY--;
				}
				if (newChoice == 8) {// en HAUT � GAUCHE
					posX--;
					posY--;
				}
				S.nbMove++;
				// On garde le co�t du chemin
				S.costPath += pLand.CostTo(landAt(posX, posY));
				// on avance le pointeur
				pLand = landAt(posX, posY);
				// on marque la carte
				pLand.isFree = false;
				lastChoice = 0;
				System.out.println("pLand : " + pLand.getName());

			}
			// on d�pile
			else {
				S.nbMove--;
				// on recupere le coup precedent
				lastSituation = S.TopStack();
				// on le supprime de la pile
				S.Pop();
				// on libere la case
				pLand.isFree = true;
				System.out.println("POP pLand : " + pLand.getName());
				// on met � jour le co�t du chemin
				S.costPath -= lastSituation.lastLand.CostTo(pLand);
				// on revient sur la case d'avant
				pLand = lastSituation.lastLand;
				posX = pLand.getXcoord();
				posY = pLand.getYcoord();
				// on se replace dans le choix Precedent
				lastChoice = lastSituation.lastChoice;
				System.out.println("pLand : " + pLand.getName());
			}
		}
		System.out.println("---------- Path Find cost : " + S.costPath);
		// on sauvgarde le meilleur chemin
		return S;
	}

	/*
	 * Trouve le chemin de moindre co�t
	 */
	private void LaunchResolution() {
		bestPath = FindPathFrom(new Situation(0, startLand));
		Situation actualSituation = bestPath.TopStack();
		Stack<Situation> S;
		do {
			
			S = FindPathFrom(actualSituation);
			
			if(bestPath.costPath > S.costPath) {
				bestPath = S;
				actualSituation = bestPath.TopStack();
			}
			else {
				actualSituation = bestPath.SituationBefore(actualSituation);
			}
			
		}while(actualSituation != null);
		
		SetFreeExcept(bestPath);
		endLand.isFree = false;
		
		System.out.println("Best cost = " + bestPath.costPath);
	}
}