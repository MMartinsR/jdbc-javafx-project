package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	/**
	 * M�todo para retornar o palco atual.
	 * Primeiramente, ele pega o objeto onde aconteceu o evento atrav�s de sua origem que � de tipo Object, 
	 * ent�o, faz o downcasting para Node, em seguida, pega a cena atrav�s desse objeto Node e em seguida a janela atrav�s da cena
	 * como a janela � uma superclasse do palco que � o que queremos, faz-se o downcasting para Stage.
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
