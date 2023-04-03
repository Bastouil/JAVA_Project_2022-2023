/*
 * INF 1401 : projet JAVA 2022-2023
 * Trouver le meilleur chemin au sein d'une grille de case avec des nombres.
 * JAOUANNE Lilian & GARCON Bastian
 */

/*
 * Situation qui sera stockée dans la pile
 */
public class Situation{
	// Attributs *******************************************************************
	int lastChoice;
	Land lastLand;
	// Constructeurs ***************************************************************
	Situation(int choice, Land land){
		lastChoice = choice;
		lastLand = land;
	}
}
