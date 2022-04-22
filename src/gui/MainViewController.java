package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();		
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();		
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized <T> void loadView (String absoluteName, Consumer<T> initializingAction) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));  // permite carregar uma tela fxml passada por parâmetro
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();  // pegou a referência para a cena principal
			// pegamos uma referência ao primeiro elemento da tela principal que é um scrollpane, então faz se casting 
			// para o compilador entender que é um scrollpane e depois pegamos o conteudo esse scrollpane.
			// E o que falta fazer é um casting para VBox, e então teremos uma referência para o VBox que está dentro do scrollpane.
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			// Vamos guardar uma referência para o menu principal, chamando o mainVBox e pegando os filhos dele, na primeira posição
			// e quem é o primeiro filho do mainVBox? é o menu principal.
			Node mainMenu = mainVBox.getChildren().get(0);
			// agora vamos limpar os filhos do mainVBox para substituir pelos filhos do newVBox
			mainVBox.getChildren().clear();
			// agora vamos colocar o menu novamente e os filhos do newVBox
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();  // retorna um controlador do tipo que for instanciado na chamada da função loadView, se passarmos um DepartmentListController, é esse que será retornado
			initializingAction.accept(controller);
	
		}
		catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
