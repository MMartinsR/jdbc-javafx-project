package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	/**
	 * Método para retornar o palco atual.
	 * Primeiramente, ele pega o objeto onde aconteceu o evento através de sua origem que é de tipo Object, 
	 * então, faz o downcasting para Node, em seguida, pega a cena atravês desse objeto Node e em seguida a janela através da cena
	 * como a janela é uma superclasse do palco que é o que queremos, faz-se o downcasting para Stage.
	 * @return
	 */
	public static Stage currentStage(ActionEvent event) {		
		return (Stage) ((Node) event.getSource()).getScene().getWindow();		
	}
	
	public static Integer tryParseToInt(String str) {
		
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e) {
			return null;
		}
	}

}
