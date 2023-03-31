/*
 * INF 1401 : projet JAVA 2022-2023
 * Trouver le meilleur chemin au sein d'une grille de case avec des nombres.
 * JAOUANNE Lilian & GARCON Bastian
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

/*
 * Interface de message à l'utilisateur
 */
public final class MessageInterface extends JDialog {
	// Attributs *******************************************************************
	// dimensions de la fenêtre de dialogue
	private final static int WIGHT_FRAME = 500;
	private final static int HEIGHT_FRAME = 200;
	private JLabel content;
	// bouton de confirmation
	private JButton okButton;

	// Constructeurs ***************************************************************
	MessageInterface(String message) {
		super();
		setTitle("Pressez le bouton \"OK\" pour sortir");
		// paramètrage de l'interface
		setSize(WIGHT_FRAME, HEIGHT_FRAME);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// paramètrage du message
		content = new JLabel(message);
		content.setHorizontalAlignment(SwingConstants.CENTER);
		// paramètrage du bouton
		okButton = new JButton();
		okButton.setText("OK");
		okButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				dispose();
			}
		});
		// ajout des éléments à l'interface
		getContentPane().add(content, BorderLayout.CENTER);
		getContentPane().add(okButton, BorderLayout.SOUTH);
		
		setVisible(true);
	}
}
