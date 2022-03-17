package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		System.out.println("onMenuItemDepartmentAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized void loadView (String absoluteName) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));  // permite carregar uma tela fxml passada por par�metro
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();  // pegou a refer�ncia para a cena principal
			// pegamos uma refer�ncia ao primeiro elemento da tela principal que � um scrollpane, ent�o faz se casting 
			// para o compilador entender que � um scrollpane e depois pegamos o conteudo esse scrollpane.
			// E o que falta fazer � um casting para VBox, e ent�o teremos uma refer�ncia para o VBox que est� dentro do scrollpane.
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			// Vamos guardar uma refer�ncia para o menu principal, chamando o mainVBox e pegando os filhos dele, na primeira posi��o
			// e quem � o primeiro filho do mainVBox? � o menu principal.
			Node mainMenu = mainVBox.getChildren().get(0);
			// agora vamos limpar os filhos do mainVBox para substituir pelos filhos do newVBox
			mainVBox.getChildren().clear();
			// agora vamos colocar o menu novamente e os filhos do newVBox
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			
			
			
			
		}
		catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
