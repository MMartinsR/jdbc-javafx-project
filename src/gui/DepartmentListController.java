package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);  // pegamos a refer�ncia para o palco atual atrav�s do evento que foi ativado nele
		Department depObj = new Department();
		createDialogForm(depObj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService (DepartmentService service) {  // acoplamento fraco, invers�o de controle, inje��o de depend�ncia
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// para inicializar as colunas da tableview
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// para obrigar a tableview a manter o tamanho igual ao da tela
		// vamos pegar uma refer�ncia pro stage atrav�s da cena principal e chamando o m�todo getwindow que � uma superclasse do stage
		// e ent�o fazendo downcasting para stage.
		Stage stage = (Stage) Main.getMainScene().getWindow();
		// Esse comando faz o tableview acompahar a altura da nossa janela.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	
	}
	
	/**
	 * M�todo respons�vel por carregar o servico de departamentos que ir� trazer todos os departamentos e atualizar esses dados na
	 * tableView.
	 */
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		
		tableViewDepartment.setItems(obsList);
		
	}
	// parentStage - palco que criou essa janela de dialogo
	private void createDialogForm(Department depObj, String absoluteName, Stage parentStage) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			// vamos pegar uma refer�ncia para o controlador da tela de formul�rio de departamento
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(depObj);
			controller.setDepartmentService(new DepartmentService());
			controller.updateFormData();
			
			// Como ser� carregado uma tela nova na frente da outra, temos que criar um novo palco
			// setar uma nova cena, j� que � um palco diferente
			// para inicializar esse dialogStage temos que passar o Stage pai para ele
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);  // trava a tela enquanto ela estiver ativa n�o pode interagir com outras
			dialogStage.showAndWait();
			
			
		} catch (IOException e) {
			
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}		
		
	}
	
	

}
