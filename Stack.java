/*
 * INF 1401 : projet JAVA 2022-2023
 * Trouver le meilleur chemin au sein d'une grille de case avec des nombres.
 * JAOUANNE Lilian & GARCON Bastian
 */

import java.util.Vector;

/*
 * Classe contenant la pile du meilleur chemin et celle du chemin courant. Elle
 * contient aussi le coût des deux piles.
 */
public class Stack<T extends Situation> {
	int costPath;
	Vector<T> table;

	int bestCostPath;
	Vector<T> bestTable;

	Stack() {
		costPath = 0;
		table = new Vector<T>();

		bestCostPath = Integer.MAX_VALUE;
		bestTable = new Vector<T>();
	}

	void Push(T x) {
		table.add(table.size(), x);
	}
	void PushBestTable(T x) {
		bestTable.add(bestTable.size(), x);
	}

	T TopStack() {
		return table.elementAt(table.size() - 1);
	}
	T TopStackBestTable() {
		return bestTable.elementAt(bestTable.size() - 1);
	}

	void Pop() {
		table.remove(table.size() - 1);
	}
	void PopBestTable() {
		bestTable.remove(bestTable.size() - 1);
	}
	void ClearTable() {
		int i = table.size();
		while (i > 0) {
			Pop();
			i--;
		}
	}
	void ClearBestTable() {
		int i = bestTable.size();
		while (i > 0) {
			PopBestTable();
			i--;
		}
	}
	void Clear() {
		ClearTable();
		ClearBestTable();
		costPath = 0;
		bestCostPath = Integer.MAX_VALUE;
	}
	/*
	 * Renvoie une copie de la pile jusqu'à avant la situation donnée en partant du
	 * bas de la pile. Attention, le coût n'est pas mis à jour.
	 */
	void DuplicateBestTableIntoTableUntil(T elementToStop) {
		ClearTable();
		if (bestTable.size() <= 0) {
			System.out.println("La meilleur pile est vide -> \"DuplicateBestTableIntoTableUntil\" stop");
			return;
		}
		T actualElement;
		int i = 0;
		actualElement = bestTable.elementAt(i);
		while (actualElement != elementToStop) {
			Push(DuplicateElement(actualElement));
			i++;
			actualElement = bestTable.elementAt(i);
		}
	}
	/*
	 * Renvoie une copie de la pile en partant du bas de la pile. Attention, le coût
	 * n'est pas mis à jour.
	 */
	void DuplicateTableIntoBestTable() {
		ClearBestTable();
		T element;
		int i = 0;
		int sizeTable = table.size();
		while (i < sizeTable) {
			element = table.elementAt(i);
			PushBestTable(DuplicateElement(element));
			i++;
		}
	}

	/*
	 * Duplique un élément T
	 */
	T DuplicateElement(T x) {
		int choice = x.lastChoice;
		Land land = x.lastLand;
		
		return (T) new Situation(choice, land);
	}

	/*
	 * Renvoie l'élément avant celle donnée en paramètre
	 */
	T ElementBefore(Vector<T> t, T elementToStop) {
		if (t.size() <= 0) {
			System.out.println("La pile donnee est vide -> \"ElementBefore\" stop");
			return null;
		}
		int i = 0;
		T previousElement = null;
		T situation = t.elementAt(i);
		while (situation != elementToStop) {
			previousElement = t.elementAt(i);
			i++;
			situation = t.elementAt(i);
		}
		return previousElement;
	}
}
