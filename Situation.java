
public class Situation {
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
