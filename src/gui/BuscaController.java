package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.entities.Delivery;
import model.service.ConsigneeService;
import model.service.DeliveryService;
import model.service.LocalizationService;
import model.service.SenderService;

public class BuscaController implements Initializable{

	private DeliveryService service;

	@FXML
	private ComboBox<String> comboBoxFiltro;
	
	@FXML
	private TextField txtPalavraFiltro;
	
	@FXML
	private Button btFiltro;
	
	@FXML
	private TableView<Delivery> tableViewBusca;
	
	@FXML
	private TableColumn<Delivery, String> tableColumnId;
	
	@FXML
	private TableColumn<Delivery, String> tableColumnDestinatario;
	
	@FXML
	private TableColumn<Delivery, String> tableColumnRemetente;
	
	@FXML
	private TableColumn<Delivery, String> tableColumnEndereco;
	
	@FXML
	private TableColumn<Delivery, Delivery> tableColumnEdit;
	
	private ObservableList<Delivery> obsList;
	
	@FXML
	public void onComboBoxFiltroAction() {
		System.out.println("onComboBoxFiltroAction");
	}
	
	@FXML
	public void onBtFiltroAction() {
		System.out.println("onBtFiltroAction");
	}

	public void setDeliveryService(DeliveryService service) {
		this.service = service;
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Delivery> deliveries = service.findAll();
		obsList = FXCollections.observableArrayList(deliveries);
		tableViewBusca.setItems(obsList);
		initEditButtons();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Delivery, Delivery>() {
			private final Button btEditar = new Button("Editar");

			@Override
			protected void updateItem(Delivery obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(btEditar);
				btEditar.setPrefHeight(26.0);
				btEditar.setPrefWidth(65.0);
				btEditar.setFont(new Font("Arial", 12));
				btEditar.setOnAction(
						event -> loadView(obj));
			}
		});
	}

	private synchronized void loadView(Delivery obj) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Cadastro.fxml"));
			AnchorPane newPage = loader.load();

			CadastroController controller = loader.getController();
			controller.setSender(obj.getSender());
			controller.setConsignee(obj.getConsignee());
			controller.setLocalization(obj.getLocalization());
			controller.setDelivery(obj);
			controller.setSenderService(new SenderService());
			controller.setConsigneeService(new ConsigneeService());
			controller.setLocalizationService(new LocalizationService());
			controller.setDeliveryService(new DeliveryService());

			controller.updateFormData();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			mainVBox.getChildren().set(1, newPage);
		} catch (IOException e) {
			Alerts.showAlert("RapidaEntrega", null, "Erro de carregamento de Página", AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeTable();
	}
	
	private void initializeTable() {
		tableColumnId.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().getConsignee().getId().toString()));
		tableColumnDestinatario
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsignee().getName()));
		tableColumnRemetente
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSender().getName()));
		tableColumnEndereco
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocalization().toString()));
	}
}
