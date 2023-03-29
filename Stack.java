import java.util.Vector;

public class Stack<T> {
	int nbMove;
	int costPath;
	Vector<T> table;

	Stack() {
		nbMove = 0;
		costPath = 0;
		table = new Vector<T>();
	}

	void Push(T x) {
		table.add(table.size(), x);
	}

	T TopStack() {
		return table.elementAt(table.size() - 1);
	}

	void Pop() {
		table.remove(table.size() - 1);
	}
	/*
	 * Renvoie une copie de la pile jusqu'à avant la situation donnée en partant du
	 * bas de la pile
	 */
	Stack<Situation> DuplicateStackSituationUntil(Situation situationToStop) {
		Stack<Situation> newStack = new Stack<Situation>();
		Situation actualSituation;
		newStack.nbMove = 0;
		newStack.costPath = 0;
		int i = 0;
		actualSituation = (Situation) table.elementAt(i);
		while (actualSituation != situationToStop){
			newStack.Push(actualSituation.DuplicateSituation());
			newStack.nbMove++;
			if (i >= 1)
				costPath += ((Situation)table.elementAt(i-1)).lastLand.CostTo(actualSituation.lastLand);
			i++;
			if (i < table.size())
				actualSituation = (Situation) table.elementAt(i);
		}
		
		return newStack;
	}
	/*
	 * Renvoie la situation avant celle donnée en paramètre
	 */
	Situation SituationBefore(Situation situationToStop) {
		int i = 0;
		Situation previousSituation = null;
		Situation situation = (Situation) table.elementAt(i);
		while(situation != situationToStop) {
			previousSituation = (Situation) table.elementAt(i);
			i++;
			situation = (Situation) table.elementAt(i);
		}
		return previousSituation;
	}
}