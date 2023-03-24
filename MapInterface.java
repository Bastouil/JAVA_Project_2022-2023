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
	// codes utilisées pour saisir les différents listeners
	public static final int EDIT_ACTION = 1;
	public static final int START_ACTION = 2;
	public static final int END_ACTION = 3;
	// taille des côtées de la fenêtre
	private static final int SIZE_FRAME = 600;
	protected int nbRows;
	protected int nbCols;
	protected BarMenu barMenu;
	protected Land[][] land;
	protected Land startLand;
	protected Land endLand;
	protected JPanel frame;
	protected JLabel message;

	// Constructeurs ***************************************************************
	MapInterface(int nbRows, int nbCols) {
		// paramètrage de l'interface
		super("Interface principale");
		setSize(SIZE_FRAME, SIZE_FRAME);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// instanciation du message
		message = new JLabel("Cliquez sur une case pour la modifier");
		message.setHorizontalAlignment(message.CENTER);
		// on garde les dimensions de la carte
		this.nbRows = nbRows;
		this.nbCols = nbCols;

		// instanciation du panneau
		frame = new JPanel(new GridLayout(nbRows, nbCols));

		// instanciation du tableau de cases
		land = new Land[nbRows][nbCols];
		for (int i = 0; i < nbRows; i++) {
			for (int j = 0; j < nbCols; j++) {
				// instanciation d'un case
				land[i][j] = new Land(this, i, j);
				// ajout de du bouton au panneau
				frame.add(land[i][j]);
				land[i][j].addMouseListener(Land.editLandListener);
			}
		}
		// départ et arrivée de base sur la case (0;0)
		startLand = land[0][0];
		endLand = land[0][0];
		// instanciation de la barre de menu
		barMenu = new BarMenu();
		setJMenuBar(barMenu);
		// ajout des listener sur les sous-menus
		barMenu.saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Enregistrer\"");
				message.setText("Enregistrement d'un fichier");
				EditInterface editer = new EditInterface(GetMapInterface());
				editer.AddListenerFileWrite(GetMapInterface());
			}
		});
		barMenu.loadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Charger un fichier\"");
				message.setText("Chargement d'un fichier");
				EditInterface editer = new EditInterface(GetMapInterface());
				editer.AddListenerFileRead(GetMapInterface());
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
				// TODO
				
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
	 * Met le message par défaut sur le JLabel
	 */
	public void DefaultMessage() {
		message.setText("Cliquez sur une case pour la modifier");
	}
	/*
	 * Met à jour la couleur de la carte
	 */
	protected void UpdateMapColor() {
		for (int j = 0; j < land[0].length; j++) {
			for (int i = 0; i < land.length; i++) {
				land[i][j].UpdateAppearance();
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
	// TODO
	public void DefineLandsListener(int CdeListener) {
		if(CdeListener == EDIT_ACTION) {
			for (int i = 0; i < nbRows; i++) {
				for (int j = 0; j < nbCols; j++) {
					for (MouseListener oldListener : land[i][j].getMouseListeners()) {
						land[i][j].removeMouseListener(oldListener);
					}
					land[i][j].addMouseListener(Land.editLandListener);
				}
			}
		}
		else if(CdeListener == START_ACTION) {
			for (int i = 0; i < nbRows; i++) {
				for (int j = 0; j < nbCols; j++) {
					for (MouseListener oldListener : land[i][j].getMouseListeners()) {
						land[i][j].removeMouseListener(oldListener);
					}
					land[i][j].addMouseListener(Land.startLandListener);
				}
			}
		}
		else if(CdeListener == END_ACTION) {
			for (int i = 0; i < nbRows; i++) {
				for (int j = 0; j < nbCols; j++) {
					for (MouseListener oldListener : land[i][j].getMouseListeners()) {
						land[i][j].removeMouseListener(oldListener);
					}
					land[i][j].addMouseListener(Land.endLandListener);
				}
			}
		}
	}
}
