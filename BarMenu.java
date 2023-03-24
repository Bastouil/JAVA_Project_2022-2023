import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Color.*;
import java.util.*;

import java.lang.Math;
import javax.swing.*;

/*
 * Barre de menu de l'interface de la carte
 */
public class BarMenu extends JMenuBar {
	// Attributs *******************************************************************
	private JMenu file;
	private JMenu resolution;

	public JMenuItem saveFile;
	public JMenuItem loadFile;
	public JMenuItem selectStart;
	public JMenuItem selectEnd;
	public JMenuItem launchResolution;

	// Constructeurs ***************************************************************
	BarMenu() {
		super();
		// création des menus
		file = new JMenu("Fichier");
		resolution = new JMenu("Resolution");

		// création des sous-menus
		saveFile = new JMenuItem("Enregistrer");
		loadFile = new JMenuItem("Charger");
		selectStart = new JMenuItem("Selectionner le depart");
		selectEnd = new JMenuItem("Selectionner l'arrivée");
		launchResolution = new JMenuItem("Lancer la resolution");

		// on ajoute les sous-menus aux différents menus
		file.add(saveFile);
		file.add(loadFile);
		resolution.add(selectStart);
		resolution.add(selectEnd);
		resolution.addSeparator();
		resolution.add(launchResolution);

		// on ajoute des raccourcis pour les menus
		file.setMnemonic('F');
		resolution.setMnemonic('R');

		// on ajoute les menus à la barre de menus
		add(file);
		add(resolution);
	}

	// Methodes ********************************************************************
}
