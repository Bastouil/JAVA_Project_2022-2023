/*
 * INF 1401 : projet JAVA 2022-2023
 * Trouver le meilleur chemin au sein d'une grille de case avec des nombres.
 * JAOUANNE Lilian & GARCON Bastian
 */

/*
 * Situation qui sera stockée dans la pile
 */
public class Situation{
	int lastChoice;
	Land lastLand;
	
	Situation(int choice, Land land){
		lastChoice = choice;
		lastLand = land;
	}
	/*
	 * Renvoie une copie de la situation
	 */
	Situation DuplicateSituation() {
		return new Situation(lastChoice, lastLand);
	}
}
