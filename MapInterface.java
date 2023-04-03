/*
 * INF 1401 : projet JAVA 2022-2023
 * Trouver le meilleur chemin au sein d'une grille de case avec des nombres.
 * JAOUANNE Lilian & GARCON Bastian
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/*
 * Interface de la carte.
 */
public class MapInterface extends JFrame {
	// Attributs *******************************************************************
	// codes utilisées pour saisir les différents listeners sur les cases
	protected static final int EDIT_ACTION = 1;
	protected static final int START_ACTION = 2;
	protected static final int END_ACTION = 3;
	// taille des côtées de la fenêtre
	private static final int SIZE_FRAME = 600;
	// taille de la carte
	protected int nbRows;
	protected int nbCols;
	// barre de menu
	protected BarMenu barMenu;
	// tableau de case
	protected Land[][] land;
	// case de départ
	protected Land startLand;
	// case d'arrivée
	protected Land endLand;
	// grille dans laquelle on vient mettre les cases
	protected JPanel frame;
	// message qui s'affiche à l'utilisateur
	protected JLabel message;
	// piles utilisées pour trouver le meilleur chemin
	protected Stack<Situation> stack;

	// Constructeurs ***************************************************************
	MapInterface(int nbRows, int nbCols) {
		// paramètrage de l'interface
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
		// instanciation des cases non visibles (cases en dehors de la carte)
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
				// instanciation d'une case
				land[i + 2][j + 2] = new Land(this, i + 2, j + 2);
				// ajout du bouton au panneau
				frame.add(landAt(i, j));
				landAt(i, j).addMouseListener(Land.editLandListener);
			}
		}
		// départ et arrivée initialisés en dehors de la carte
		startLand = landAt(-2, -2);
		endLand = landAt(-2, -2);
		// création de la pile
		stack = new Stack();
		// instanciation de la barre de menu
		barMenu = new BarMenu();
		setJMenuBar(barMenu);
		// ajout des listener sur les sous-menus
		barMenu.saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Enregistrer\"");
				message.setText("Enregistrement d'un fichier");
				EditInterface editer = new EditInterface(GetMapInterface());
				editer.AddListenerWriteFile();
			}
		});
		barMenu.loadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Charger un fichier\"");
				message.setText("Chargement d'un fichier");
				EditInterface editer = new EditInterface(GetMapInterface());
				editer.AddListenerReadFile();
			}
		});
		barMenu.selectStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Selectionner le depart\"");
				message.setText("Veuillez selectionner la case de départ");
				// change les listeners ce trouvant sur les boutons
				DefineLandsListener(START_ACTION);
			}
		});
		barMenu.selectEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Selectionner l'arrivee\"");
				message.setText("Veuillez selectionner la case d'arrivée");
				// change les listeners ce trouvant sur les boutons
				DefineLandsListener(END_ACTION);
			}
		});
		barMenu.launchResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Lancer la resolution\"");
				// on regarde si le départ et l'arrivée sont sur la carte
				if (!startLand.inMap || !endLand.inMap) {
					System.out.println("Lancement de resolution refusee");
					MessageInterface m = new MessageInterface(
							"Veuillez sélectionner un case de départ et d'arrivée avant de lancer la résolution");
					return;
				}
				// on regarde s'il existe un chemin
				if(There_is_NO_Path()) {
					setAllLandFree();
					System.out.println("Aucun chemin !");
					MessageInterface m = new MessageInterface(
							"Pas de chemin existant. Veuillez changer votre carte.");
					ColorMapHeight();
					return;
				}
				message.setText("Résolution en cours ...");
				LaunchResolution();
				MessageInterface m = new MessageInterface(
						"Résolution terminée. Le coût du meilleur chemin est de : " + stack.bestCostPath);
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

		// ajout du panneau et de la barre de menu à l'interface
		getContentPane().add(frame, BorderLayout.CENTER);
		getContentPane().add(barMenu, BorderLayout.NORTH);
		getContentPane().add(message, BorderLayout.SOUTH);
		setVisible(true);
	}

	// Methodes ********************************************************************
	/*
	 * Renvoie la case à partir des coordonnées visibles par l'utilisateur
	 */
	public Land landAt(int x, int y) {
		return land[x + 2][y + 2];
	}

	/*
	 * Libère toutes les cases de la carte
	 */
	public void setAllLandFree() {
		for (int i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbCols; j++) {
				landAt(i, j).isFree = true;
			}
		}
	}

	/*
	 * Occupe toutes les cases appartenants à la pile donnéeS
	 */
	void SetNotFreeTable(Vector<Situation> V) {
		for (int i = 0; i < V.size(); i++) {
			V.elementAt(i).lastLand.isFree = false;
		}
	}

	/*
	 * Met le message par défaut sur le JLabel
	 */
	public void DefaultMessage() {
		message.setText("Cliquez sur une case pour la modifier");
	}

	/*
	 * Met à jour la couleur de la carte selon la hauteur des cases
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
	 * Met à jour la couleur de la carte selon le chemin
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
	 * Color la carte d'une couleur donnée
	 */
	protected void ColorMap(Color x) {
		for (int i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbCols; j++) {
				landAt(i, j).ColorWith(x);
			}
		}
	}

	/*
	 * Récuperer le pointeur du la structure MapInterface
	 */
	private MapInterface GetMapInterface() {
		return this;
	}

	/*
	 * Définit les listeners à mettre sur les boutons (objets de type Land)
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
	 * Renvoie le coût d'une table donnée
	 */
	private int CostOfTable(Vector<Situation> table, Situation nextSituation) {
		if (table.size() <= 0) {
			return 0;
		}
		int costPath = 0;
		for (int i = 1; i < table.size(); i++) {
			costPath += table.elementAt(i - 1).lastLand.CostTo(table.elementAt(i).lastLand);
		}
		costPath += table.elementAt(table.size() - 1).lastLand.CostTo(nextSituation.lastLand);
		return costPath;
	}

	/*
	 * Renvoie le choix possible
	 */
	private int NextPossibleChoice(int posX, int posY, int lastChoice, Land actualLand) {
		if (lastChoice == 0) {
			if (landAt(posX - 1, posY).isFree && actualLand.IsPassableTo(landAt(posX - 1, posY)))
				return 1; // en HAUT
			if (landAt(posX, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX, posY + 1)))
				return 2; // à DROITE
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 3; // en BAS
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 4; // à GAUCHE
		}
		if (lastChoice == 1) {
			if (landAt(posX, posY + 1).isFree && actualLand.IsPassableTo(landAt(posX, posY + 1)))
				return 2; // à DROITE
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 3; // en BAS
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 4; // à GAUCHE
		}
		if (lastChoice == 2) {
			if (landAt(posX + 1, posY).isFree && actualLand.IsPassableTo(landAt(posX + 1, posY)))
				return 3; // en BAS
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 4; // à GAUCHE
		}
		if (lastChoice == 3) {
			if (landAt(posX, posY - 1).isFree && actualLand.IsPassableTo(landAt(posX, posY - 1)))
				return 4; // à GAUCHE
		}

		return -1;
	}

	/*
	 * Trouve un chemin jusqu'à l'arrivée à partir de la situation donnée
	 */
	private void FindPathFrom(Situation situation) {
		Situation lastSituation = situation;
		Land pLand = lastSituation.lastLand;
		// coordonnée de pLand
		int posX = pLand.getXcoord();
		int posY = pLand.getYcoord();
		int lastChoice = lastSituation.lastChoice;
		int newChoice;

		// on copie la meilleur pile dans l'autre pile
		stack.DuplicateBestTableIntoTableUntil(situation);
		// libère toutes les cases sauf celle de la pile du chemin courant
		setAllLandFree();
		SetNotFreeTable(stack.table);
		// on calcul de coût du chemin se trouvant dans la pile courante
		stack.costPath = CostOfTable(stack.table, situation);
		pLand.isFree = false;
		while (endLand.isFree) {
			// cherche le déplacement suivant
			newChoice = NextPossibleChoice(posX, posY, lastChoice, pLand);

			if (newChoice != -1 && stack.bestCostPath > stack.costPath) {
				// on empile le deplacement
				stack.Push(new Situation(newChoice, pLand));
				// on va sur la nouvelle case
				if (newChoice == 1) {// en HAUT
					posX--;
				}
				if (newChoice == 2) {// à DROITE
					posY++;
				}
				if (newChoice == 3) {// en BAS
					posX++;
				}
				if (newChoice == 4) {// à GAUCHE
					posY--;
				}
				// On garde le coût du chemin
				stack.costPath += pLand.CostTo(landAt(posX, posY));
				// on avance le pointeur
				pLand = landAt(posX, posY);
				// on marque la carte
				pLand.isFree = false;
				lastChoice = 0;

			}
			// on dépile si la pile n'est pas vide
			else if (!stack.table.isEmpty()) {
				// on recupere le coup precedent
				lastSituation = stack.TopStack();
				// on le supprime de la pile
				stack.Pop();
				// on libere la case
				pLand.isFree = true;
				// on met à jour le coût du chemin
				stack.costPath -= lastSituation.lastLand.CostTo(pLand);
				// on revient sur la case d'avant
				pLand = lastSituation.lastLand;
				posX = pLand.getXcoord();
				posY = pLand.getYcoord();
				// on se replace dans le choix Precedent
				lastChoice = lastSituation.lastChoice;
				// sinon on sort de la fonction avec le pire coût
			} else {
				stack.costPath = Integer.MAX_VALUE;
				return;
			}
		}
	}

	/*
	 * Trouve le chemin de moindre coût
	 */
	private void LaunchResolution() {
		stack.Clear();
		Situation actualSituation = new Situation(0, startLand);
		do {
			FindPathFrom(actualSituation);
			if (stack.bestCostPath >= stack.costPath) {
				stack.DuplicateTableIntoBestTable();
				stack.bestCostPath = stack.costPath;
				actualSituation = stack.TopStackBestTable();
			} else {
				actualSituation = stack.ElementBefore(stack.bestTable, actualSituation);
			}
		} while (actualSituation != null && stack.bestCostPath != 0);
		setAllLandFree();
		SetNotFreeTable(stack.bestTable);
		endLand.isFree = false;
		PrintTable(stack.bestTable);
	}

	/*
	 * Renvoie un boolean disant si il y a ou non un chemin possible.
	 * true : il n'y a pas de chemin
	 * false : il y a au moins un chemin
	 */
	private boolean There_is_NO_Path() {
		stack.Clear();
		FindPathFrom(new Situation(0, startLand));

		if (stack.bestCostPath == stack.costPath)
			return true;
		else
			return false;
	}

	/*
	 * Affiche le contenu d'une pile
	 */
	private void PrintTable(Vector<Situation> table) {
		System.out.println("Pile : ******************");
		for (int i = 0; i < table.size(); i++) {
			table.elementAt(i).lastLand.isFree = false;
			System.out.println(table.elementAt(i).lastChoice + "	" + table.elementAt(i).lastLand.getName());
		}
		System.out.println("*************************");
	}
}
