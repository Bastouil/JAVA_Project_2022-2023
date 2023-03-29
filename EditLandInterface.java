import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

/*
 * Interface de modification
 */
public class EditLandInterface extends JDialog {
	// Attributs *******************************************************************
	// code erreure
	private final static int NOT_A_INT = -1;
	private final static String FILE_NAME_NOT_VALIDE = "";
	// dimensions de la fen�tre de dialogue
	private final static int WIGHT_FRAME = 300;
	private final static int HEIGHT_FRAME = 120;
	// label qui va contenir le titre
	private JLabel message;
	private JLabel text;
	// zone de texte o� va �crire l'utilisateur
	private JTextField textField;
	// bouton de confirmation
	private JButton okButton;
	// nom du fichier avec lequel int�ragire
	private String fileName;
	// source du d�clanchement du listener
	Land landSource;
	MapInterface mapSource;
	// instanciation des diff�rents listeners
	private final KeyListener keyListenerClose = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				MyDispose();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	};
	private final MouseAdapter mouseListenerClose = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			MyDispose();
		}
	};
	private final KeyListener keyListenerEditLand = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				ChangeLandNumber();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	};
	private final KeyListener keyListenerReadFile = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				ReadMapInTextFile();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	};
	private final KeyListener keyListenerWriteFile = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				WriteMapInTextFile();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	};
	private final MouseAdapter mouseListenerEditLand = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			ChangeLandNumber();
		}
	};
	private final MouseAdapter mouseListenerReadFile = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			ReadMapInTextFile();
		}
	};
	private final MouseAdapter mouseListenerWriteFile = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			WriteMapInTextFile();
		}
	};

	// Constructeurs ***************************************************************

	EditLandInterface(Land land) {
		// param�trage de l'interface
		super();
		setTitle("Pressez la touche ESC pour sortir");
		// r�cup�ration de la source
		landSource = land;
		mapSource = land.map;
		setSize(WIGHT_FRAME, HEIGHT_FRAME);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// instanciation du titre
		message = new JLabel("Entrez un entier entre 0 et 30 :");
		// instanciation de la zone de texte
		textField = new JTextField();
		textField.addKeyListener(keyListenerEditLand);
		textField.addKeyListener(keyListenerClose);
		// instanciation et param�trage du bouton OK
		okButton = new JButton();
		okButton.setText("OK");
		okButton.addMouseListener(mouseListenerEditLand);		
		// ajout du titre, de la zone de texte et du bouton � l'interface
		getContentPane().add(message, BorderLayout.NORTH);
		getContentPane().add(textField, BorderLayout.CENTER);
		getContentPane().add(okButton, BorderLayout.SOUTH);

		setVisible(true);
	}

	EditLandInterface(MapInterface map) {
		// param�trage de l'interface
		super();
		setTitle("Pressez la touche ESC pour sortir");
		// r�cup�ration de la source
		mapSource = map;
		landSource = map.landAt(0 , 0);
		setSize(WIGHT_FRAME, HEIGHT_FRAME);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// instanciation du titre
		message = new JLabel("Entrez un nom de fichier valide (sans le \".txt\") :");
		// instanciation de la zone de texte
		textField = new JTextField();
		textField.addKeyListener(keyListenerClose);
		// instanciation et param�trage du bouton
		okButton = new JButton();
		okButton.setText("OK");
		// ajout du titre, de la zone de texte et du bouton � l'interface
		getContentPane().add(message, BorderLayout.NORTH);
		getContentPane().add(textField, BorderLayout.CENTER);
		getContentPane().add(okButton, BorderLayout.SOUTH);

		setVisible(true);
	}
	
	EditLandInterface(MapInterface map, String titreAbout) {
		// param�trage de l'interface de la fenetre help
		super();
		setTitle("Appuyer sur OK pour sortir");
		// r�cup�ration de la source
		mapSource = map;
		landSource = map.landAt(0 , 0);
		setSize(WIGHT_FRAME+20, HEIGHT_FRAME+70);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// instanciation du titre
		message = new JLabel(titreAbout);
		// instanciation en html de la zone de texte
		text = new JLabel("<html>Version: (0.0.1)<br><br>Date: 2023<br>(c) Copyright App contributors and others.<br>All rights reserved.<br><br>By Gar�on Bastian & Jaouanne Lilian</html>");
		okButton = new JButton();
		okButton.setText("OK");
		okButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 MyDispose(); // Ferme la fen�tre actuelle
	         }
	      });
		// ajout du titre, de la zone de texte et du bouton � l'interface
		getContentPane().add(message, BorderLayout.NORTH);
		getContentPane().add(text, BorderLayout.CENTER);
		getContentPane().add(okButton, BorderLayout.SOUTH);

		setVisible(true);
	}

	// Methodes ********************************************************************
	/*
	 * Fermer proprement la fen�tre de dialogue
	 */
	private void MyDispose() {
		mapSource.DefaultMessage();
		dispose();
	}
	/*
	 * Ajoute les deux listeners n�cessaire � l'�criture dans un fichier texte
	 */
	public void AddListenerWriteFile() {
		addKeyListener(keyListenerWriteFile);
		okButton.addMouseListener(mouseListenerWriteFile);	
	}
	/*
	 * Ajoute les deux listeners n�cessaire � la lecture d'un fichier texte
	 */
	public void AddListenerReadFile() {
		addKeyListener(keyListenerReadFile);
		okButton.addMouseListener(mouseListenerReadFile);	
	}
	/*
	 * R�cup�re l'entier contenu dans la zone de texte
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
	private void ChangeLandNumber() {
		int newInt = getInt();
		if (newInt != NOT_A_INT) {
			landSource.setText("" + newInt);
			landSource.height = newInt;
			landSource.UpdateAppearance();
			MyDispose();
		}
	}
	/*
	 * R�cup�re la cha�ne de caract�re contenu dans la zone de texte et v�rifie si
	 * c'est un nom de fichier valide (sans le ".txt" � la fin)
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
	 * Dit si le caract�re est valide ou non pour un nom de fichier
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
	private void WriteMapInTextFile() {
		String str = GetValideFileName(textField.getText());
		if (str == FILE_NAME_NOT_VALIDE) {
			return;
		}

		fileName = str + ".txt";
		try {
			FileWriter myFile = new FileWriter(fileName);

			myFile.write(mapSource.nbRows + "\n");
			myFile.write(mapSource.nbCols + "\n");

			for (int i = 0; i < mapSource.nbCols; i++) {
				for (int j = 0; j < mapSource.nbRows; j++) {
					myFile.write(mapSource.landAt(i, j).height + "\n");
				}
			}
			myFile.write("$");

			myFile.close();
		} catch (IOException e) {
			// erreur dans l'ouverture du fichier
			System.out.println("Echec de l'ouverture du fichier : " + fileName);
		}
		MyDispose();
	}
	/*
	 * Lit le contenut d'un fichier texte pour le mettre dans une carte
	 */
	private void ReadMapInTextFile() {
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
			// on r�cup�re la taille de la carte
			try {
				nbRows = Integer.parseInt(myFile.readLine());
				nbCols = Integer.parseInt(myFile.readLine());
			} catch (NumberFormatException e) {
				System.out.println("Echec de lecture des deux premiers entiers dans le fichier : " + fileName);
				nbRows = 1;
				nbCols = 1;
			}
			// on cr�e une nouvelle carte qui servira � charger les valeurs
			MapInterface newMap = new MapInterface(nbRows, nbCols);
			// on rentre les donn�es du fichier dans la carte
			for (i = 0; i < nbRows; i++) {
				for (j = 0; j < nbCols; j++) {
					try {
						newMap.landAt(i, j).height = Integer.parseInt(myFile.readLine());
						newMap.landAt(i, j).UpdateAppearance();
					} catch (NumberFormatException e) {
						System.out.println("Echec de lecture de l'entier a la ligne " + (i * nbCols + j + 3)
						+ " du fichier : " + fileName);
						newMap.landAt(i, j).height = 0;
					}
				}
			}
			myFile.close();
			mapSource.dispose();
			mapSource = newMap;
			//mapSource.UpdateMapColor();
		} catch (FileNotFoundException e) {
			System.out.println("Fichier introuvable : " + fileName);
		} catch (IOException e) {
			System.out.println("Echec de l'ouverture du fichier : " + fileName);
		}
		MyDispose();
	}
}