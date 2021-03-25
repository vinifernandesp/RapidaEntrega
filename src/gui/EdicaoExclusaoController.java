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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.entities.Delivery;
import model.service.DeliveryService;

public class EdicaoExclusaoController implements Initializable{

	private DeliveryService service;
	
	@FXML
	private TableView<Delivery> tableViewEdicaoExclusao;
	
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
	
	@FXML
	private Button btEditar;
	
	@FXML
	private Button btExcluir;
	
	private ObservableList<Delivery> obsList;
	
	@FXML
	public void onBtEditarAction() {
		System.out.println("onBtEditarAction");
	}
	
	@FXML
	public void onBtExcluirAction() {
		System.out.println("onBtExcluirAction");	
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
		tableViewEdicaoExclusao.setItems(obsList);
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
