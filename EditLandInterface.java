import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

/*
 * Interface de modification
 */
public class EditInterface extends JDialog {
	// Attributs *******************************************************************
	// code erreure
	protected final static int NOT_A_INT = -1;
	protected final static String FILE_NAME_NOT_VALIDE = "";
	// dimensions de la fenêtre de dialogue
	private final static int WIGHT_FRAME = 250;
	private final static int HEIGHT_FRAME = 100;
	// label qui va contenir le titre
	protected JLabel title;
	// zone de texte où va écrire l'utilisateur
	protected JTextField textField;
	// bouton de confirmation
	protected JButton okButton;
	// nom du fichier avec lequel intéragire
	protected String fileName;

	// Constructeurs ***************************************************************

	EditInterface(Land landSource) {
		// paramètrage de l'interface
		super();
		setSize(WIGHT_FRAME, HEIGHT_FRAME);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// instanciation du titre
		title = new JLabel("Entrez un entier entre 0 et 30 :");
		// instanciation de la zone de texte
		textField = new JTextField();
		textField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ChangeLandNumber(landSource);
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		// instanciation et paramètrage du bouton
		okButton = new JButton();
		okButton.setText("OK");
		okButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				ChangeLandNumber(landSource);
			}
		});
		// ajout du titre, de la zone de texte et du bouton à l'interface
		getContentPane().add(title, BorderLayout.NORTH);
		getContentPane().add(textField, BorderLayout.CENTER);
		getContentPane().add(okButton, BorderLayout.SOUTH);

		setVisible(true);

	}

	EditInterface(MapInterface map) {
		// paramètrage de l'interface
		super();
		setSize(WIGHT_FRAME, HEIGHT_FRAME);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// instanciation du titre
		title = new JLabel("Entrez un nom de fichier valide (sans le \".txt\") :");
		// instanciation de la zone de texte
		textField = new JTextField();

		// instanciation et paramètrage du bouton
		okButton = new JButton();
		okButton.setText("OK");

		// ajout du titre, de la zone de texte et du bouton à l'interface
		getContentPane().add(title, BorderLayout.NORTH);
		getContentPane().add(textField, BorderLayout.CENTER);
		getContentPane().add(okButton, BorderLayout.SOUTH);

		setVisible(true);
	}

	// Methodes ********************************************************************
	/*
	 * Ajoute des listeners dédiés à l'écriture dans un fichier texte
	 */
	public void AddListenerFileWrite(MapInterface map) {
		textField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					WriteMapInTextFile(map);
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		okButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				WriteMapInTextFile(map);
			}
		});
	}
	/*
	 * Ajoute des listeners dédiés à la lecture dans un fichier texte
	 */
	public void AddListenerFileRead(MapInterface map) {
		textField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ReadMapInTextFile(map);
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		okButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				ReadMapInTextFile(map);
			}
		});
	}
	/*
	 * Récupère l'entier contenu dans la zone de texte
	 */
	private int getInt() {
		// contenu brut de la zone de texte
		String textContent = textField.getText();
		try {
			int num = Integer.parseInt(textContent);
			if (num < 0) {
				num = 0;
			} else if (num > 30) {
				num = 30;
			}

			return num;
		} catch (NumberFormatException e) {
			System.out.println("La chaine de caractere n'est pas un entier");
			return (NOT_A_INT);
		}
	}
	/*
	 * Execute l'action du bouton OK et de la touche entrer lorsque l'on veut une
	 * hauteur valide pour un "Land"
	 */
	public void ChangeLandNumber(Land landSource) {
		int newInt = getInt();
		if (newInt != NOT_A_INT) {
			landSource.setText("" + newInt);
			landSource.cInt = newInt;
			landSource.UpdateLandColor();
			dispose();
		}
	}
	/*
	 * Récupère la chaîne de caractère contenu dans la zone de texte et vérifie si
	 * c'est un nom de fichier valide (sans le ".txt" à la fin)
	 */
	private String GetValideFileName(String textContent) {
		char charToAnalyse;
		int sizeTextContent = textContent.length();
		for (int i = 0; i < sizeTextContent; i++) {
			charToAnalyse = textContent.charAt(i);
			if (!ValideFileChar(charToAnalyse)) {
				System.out.println("Nom de fichier invalide. Caractere invalidant : \"" + charToAnalyse + "\"");
				return FILE_NAME_NOT_VALIDE;
			}
		}
		return textContent;
	}
	/*
	 * Dit si le caractère est valide ou non pour un nom de fichier
	 */
	private boolean ValideFileChar(char c) {
		if (c == '-' || c == '_' || ('0' <= c && c <= '9') || ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
			return true;
		}
		return false;
	}
	/*
	 * Ecrit le contenut de la carte dans un fichier texte
	 */
	private void WriteMapInTextFile(MapInterface map) {
		String str = GetValideFileName(textField.getText());
		if (str == FILE_NAME_NOT_VALIDE) {
			return;
		}

		fileName = str + ".txt";
		try {
			FileWriter myFile = new FileWriter(fileName);

			myFile.write(map.nbRows + "\n");
			myFile.write(map.nbCols + "\n");

			for (int i = 0; i < map.nbCols; i++) {
				for (int j = 0; j < map.nbRows; j++) {
					myFile.write(map.land[i][j].cInt + "\n");
				}
			}
			myFile.write("$");

			myFile.close();
		} catch (IOException e) {
			// erreur dans l'ouverture du fichier
			System.out.println("Echec de l'ouverture du fichier : " + fileName);
		}
		dispose();
	}
	/*
	 * Lit le contenut d'un fichier texte pour le mettre dans une carte
	 */
	private void ReadMapInTextFile(MapInterface map) {
		String str = GetValideFileName(textField.getText());
		if (str == FILE_NAME_NOT_VALIDE) {
			return;
		}

		fileName = str + ".txt";
		
		int i = 0, j = 0;

		try {
			BufferedReader myFile = new BufferedReader(new FileReader(fileName));
			int nbRows;
			int nbCols;
			// on récupère la taille de la carte
			try {
				nbRows = Integer.parseInt(myFile.readLine());
				nbCols = Integer.parseInt(myFile.readLine());
			} catch (NumberFormatException e) {
				System.out.println("Echec de lecture des deux premiers entiers dans le fichier : " + fileName);
				nbRows = 1;
				nbCols = 1;
			}
			// on crée une nouvelle carte qui servira à charger les valeurs
			MapInterface newMap = new MapInterface(nbRows, nbCols);
			// on rentre les données du fichier dans la carte
			for (i = 0; i < nbRows; i++) {
				for (j = 0; j < nbCols; j++) {
					try {
						newMap.land[i][j].cInt = Integer.parseInt(myFile.readLine());
					} catch (NumberFormatException e) {
						System.out.println("Echec de lecture de l'entier a la ligne " + (i * nbCols + j + 3)
						+ " du fichier : " + fileName);
						newMap.land[i][j].cInt = 0;
					}
				}
			}
			myFile.close();
			map.dispose();
			map = newMap;
			map.UpdateMapColor();
		} catch (FileNotFoundException e) {
			System.out.println("Fichier introuvable : " + fileName);
		} catch (IOException e) {
			System.out.println("Echec de l'ouverture du fichier : " + fileName);
		}
		dispose();
	}
}
