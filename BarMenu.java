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
	private JMenu coloring;

	public JMenuItem saveFile;
	public JMenuItem loadFile;
	public JMenuItem selectStart;
	public JMenuItem selectEnd;
	public JMenuItem launchResolution;
	public JCheckBoxMenuItem displayPathColor;
	public JCheckBoxMenuItem displayHeightColor;

	// Constructeurs ***************************************************************
	BarMenu() {
		super();
		// création des menus
		file = new JMenu("Fichier");
		resolution = new JMenu("Résolution");
		coloring = new JMenu("Coloration");

		// création des sous-menus
		saveFile = new JMenuItem("Enregistrer");
		loadFile = new JMenuItem("Charger");
		selectStart = new JMenuItem("Sélectionner le départ");
		selectEnd = new JMenuItem("Sélectionner l'arrivée");
		launchResolution = new JMenuItem("Lancer la résolution");
		displayPathColor = new JCheckBoxMenuItem("Chemin");
		displayHeightColor = new JCheckBoxMenuItem("Hauteur");
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(displayPathColor);
		bg.add(displayHeightColor);
		displayHeightColor.setSelected(true);

		// on ajoute les sous-menus aux différents menus
		file.add(saveFile);
		file.add(loadFile);
		resolution.add(selectStart);
		resolution.add(selectEnd);
		resolution.addSeparator();
		resolution.add(launchResolution);
		coloring.add(displayPathColor);
		coloring.add(displayHeightColor);

		// on ajoute les menus à la barre de menus
		add(file);
		add(resolution);
		add(coloring);
	}

	// Methodes ********************************************************************
}
