package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener{
	
	private SellerService service;
	
	@FXML
	private TableView<Seller> tableViewSeller;
	
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);  // pegamos a referência para o palco atual através do evento que foi ativado nele
		Seller depObj = new Seller();
		createDialogForm(depObj, "/gui/SellerForm.fxml", parentStage);
	}
	
	public void setSellerService (SellerService service) {  // acoplamento fraco, inversão de controle, injeção de dependência
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// para inicializar as colunas da tableview
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));  // como na classe seller, tem que estar igual 
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		// para obrigar a tableview a manter o tamanho igual ao da tela
		// vamos pegar uma referência pro stage através da cena principal e chamando o método getwindow que é uma superclasse do stage
		// e então fazendo downcasting para stage.
		Stage stage = (Stage) Main.getMainScene().getWindow();
		// Esse comando faz o tableview acompahar a altura da nossa janela.
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	
	}
	
	/**
	 * Método responsável por carregar o servico de departamentos que irá trazer todos os departamentos e atualizar esses dados na
	 * tableView.
	 */
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service is null");
		}
		
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		
	}
	// parentStage - palco que criou essa janela de dialogo
	private void createDialogForm(Seller depObj, String absoluteName, Stage parentStage) {
//		
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//			
//			// vamos pegar uma referência para o controlador da tela de formulário de departamento
//			SellerFormController controller = loader.getController();
//			controller.setSeller(depObj);
//			controller.setSellerService(new SellerService());
//			controller.subscribeDataChangeListener(this);  // passa para o método um objeto que implementa a interface datachangelistener, que no caso é o próprio controller.
//			controller.updateFormData();
//			
//			// Como será carregado uma tela nova na frente da outra, temos que criar um novo palco
//			// setar uma nova cena, já que é um palco diferente
//			// para inicializar esse dialogStage temos que passar o Stage pai para ele
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Enter Seller data");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);  // trava a tela enquanto ela estiver ativa não pode interagir com outras
//			dialogStage.showAndWait();
//			
//			
//		} catch (IOException e) {
//			
//			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}		
//		
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	// método para criar os botões edit ao lado de cada linha da tableview e carregar a tela de formulario com os dados do 
	// departamento relativo ao botao edit que foi clicado.
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(
								obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	// método para criar os botões remove ao lado de cada linha da tableview e deletar o departmento caso o botão seja clicado.
	public void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	
	// Método para confirmação da deleção do departamento, caso a confirmação seja aceita, o departamento será removido.
	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service is null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch(Exception e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	

}
