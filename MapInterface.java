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
	private static final int SIZE_FRAME = 600;
	protected int nbRows;
	protected int nbCols;
	protected BarMenu barMenu;
	protected Land[][] land;
	protected Land startLand;
	protected Land endLand;
	protected JPanel frame;

	// Constructeurs ***************************************************************
	MapInterface(int nbRows, int nbCols) {
		// paramètrage de l'interface
		super("Interface principale");
		setSize(SIZE_FRAME, SIZE_FRAME);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
				land[i][j] = new Land(i, j);
				// ajout de du bouton au panneau
				frame.add(land[i][j]);
			}
		}

		// instanciation de la barre de menu
		barMenu = new BarMenu();
		setJMenuBar(barMenu);
		// ajout des listener sur les sous-menus
		barMenu.saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Enregistrer\"");
				EditInterface editer = new EditInterface(GetMapInterface());
				editer.AddListenerFileWrite(GetMapInterface());
			}
		});
		barMenu.loadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Charger un fichier\"");
				EditInterface editer = new EditInterface(GetMapInterface());
				editer.AddListenerFileRead(GetMapInterface());
			}
		});
		barMenu.selectSnE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"Selectionner le depart et l'arriver\"");
				// TODO
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
		setVisible(true);
	}

	// Methodes ********************************************************************
	/*
	 * Met à jour la couleur de la carte
	 */
	protected void UpdateMapColor() {
		for (int j = 0; j < land[0].length; j++) {
			for (int i = 0; i < land.length; i++) {
				land[i][j].UpdateLandColor();
			}
		}
	}

	/*
	 * Récuperer le pointeur du la structure MapInterface
	 */
	private MapInterface GetMapInterface() {
		return this;
	}
}
