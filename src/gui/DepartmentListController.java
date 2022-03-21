package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	public void setDepartmentService (DepartmentService service) {  // acoplamento fraco, inversão de controle, injeção de dependência
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
		// vamos pegar uma referência pro stage através da cena principal e chamando o método getwindow que é uma superclasse do stage
		// e então fazendo downcasting para stage.
		Stage stage = (Stage) Main.getMainScene().getWindow();
		// Esse comando faz o tableview acompahar a altura da nossa janela.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	
	}
	
	/**
	 * Método responsável por carregar o servico de departamentos que irá trazer todos os departamentos e atualizar esses dados na
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
	
	

}
