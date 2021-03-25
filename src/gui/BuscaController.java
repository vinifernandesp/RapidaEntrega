package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.entities.Delivery;
import model.service.DeliveryService;

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
	private TableColumn<Delivery, String> tableColumnIdEndereco;
	
	@FXML
	private TableColumn<Delivery, String> tableColumnEndereco;
	
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
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		tableColumnId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsignee().getId().toString()));
		tableColumnDestinatario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsignee().getName()));
		tableColumnRemetente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSender().getName()));
		tableColumnIdEndereco.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocalization().getId().toString()));
		tableColumnEndereco.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocalization().toString()));
	}
}
