/*
 * INF 1401 : projet JAVA 2022-2023
 * Trouver le meilleur chemin au sein d'une grille de case avec des nombres.
 * JAOUANNE Lilian & GARCON Bastian
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/*
 * Barre de menu de l'interface de la carte
 */
public class BarMenu extends JMenuBar {
	// Attributs *******************************************************************
	private JMenu file;
	private JMenu resolution;
	private JMenu coloring;
	private JMenu help;

	public JMenuItem saveFile;
	public JMenuItem loadFile;
	public JMenuItem selectStart;
	public JMenuItem selectEnd;
	public JMenuItem launchResolution;
	public JCheckBoxMenuItem displayPathColor;
	public JCheckBoxMenuItem displayHeightColor;
	public JMenuItem aboutHelp;

	// Constructeurs ***************************************************************
	BarMenu() {
		super();
		// création des menus
		file = new JMenu("Fichier");
		resolution = new JMenu("Résolution");
		coloring = new JMenu("Coloration");
		help = new JMenu("Aide");

		// création des sous-menus
		saveFile = new JMenuItem("Enregistrer");
		loadFile = new JMenuItem("Charger");
		selectStart = new JMenuItem("Sélectionner le départ");
		selectEnd = new JMenuItem("Sélectionner l'arrivée");
		launchResolution = new JMenuItem("Lancer la résolution");
		displayPathColor = new JCheckBoxMenuItem("Chemin");
		displayHeightColor = new JCheckBoxMenuItem("Hauteur");
		aboutHelp = new JMenuItem("A propos");

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
		help.add(aboutHelp);
		
		// ajout du listener sur l'onglet à propos
		aboutHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Choix \"A propos\"");
				MessageInterface m = new MessageInterface(
						"<html>Version: (0.0.1)<br><br>Date: 2023<br>(c) Copyright App contributors and others.<br>All rights reserved.<br><br>By Garçon Bastian & Jaouanne Lilian</html>");
			}
		});

		// on ajoute les menus à la barre de menus
		add(file);
		add(resolution);
		add(coloring);
		add(help);
	}
}
