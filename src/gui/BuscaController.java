package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.entities.Delivery;
import model.service.ConsigneeService;
import model.service.DeliveryService;
import model.service.LocalizationService;
import model.service.SenderService;

public class BuscaController implements Initializable {

	private SenderService senderService;
	private ConsigneeService consigneeService;
	private LocalizationService localizationService;
	private DeliveryService deliveryService;

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

	@FXML
	private TableColumn<Delivery, Delivery> tableColumnRemove;

	private ObservableList<Delivery> obsList;

	public void setSenderService(SenderService senderService) {
		this.senderService = senderService;
	}

	public void setConsigneeService(ConsigneeService consigneeService) {
		this.consigneeService = consigneeService;
	}

	public void setLocalizationService(LocalizationService localizationService) {
		this.localizationService = localizationService;
	}

	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	@FXML
	public void onComboBoxFiltroAction() {
		txtPalavraFiltro.setText("");
		
		if (comboBoxFiltro.getValue() == "Todos") {
			if (deliveryService == null) {
				throw new IllegalStateException("Service was null");
			}
			List<Delivery> deliveries = deliveryService.findAll();
			updateTableView(deliveries);
			
			txtPalavraFiltro.setVisible(false);
			btFiltro.setVisible(false);
		}
		else {
			txtPalavraFiltro.setVisible(true);
			btFiltro.setVisible(true);
		}
	}
	
	@FXML
	public void onBtFiltroAction() {
		if (deliveryService == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Delivery> deliveries = deliveryService.findAll();
		String research = txtPalavraFiltro.getText().trim();
		String typeOfSearch = comboBoxFiltro.getValue();
		
		if (typeOfSearch.compareToIgnoreCase("Remetente") == 0) {
			deliveries.removeIf(x -> x.getSender().getName().compareToIgnoreCase(research) != 0);
		}
		else if (typeOfSearch.compareToIgnoreCase("Destinatário") == 0) {
			deliveries.removeIf(x -> x.getConsignee().getName().compareToIgnoreCase(research) != 0);
		}
		else if (typeOfSearch.compareToIgnoreCase("País") == 0) {
			deliveries.removeIf(x -> x.getLocalization().getCountry().compareToIgnoreCase(research) != 0);
		}
		else if (typeOfSearch.compareToIgnoreCase("Estado") == 0) {
			deliveries.removeIf(x -> x.getLocalization().getState().compareToIgnoreCase(research) != 0);
		}
		else if (typeOfSearch.compareToIgnoreCase("Cidade") == 0) {
			deliveries.removeIf(x -> x.getLocalization().getCity().compareToIgnoreCase(research) != 0);
		}
		
		updateTableView(deliveries);
	}

	public void updateTableView(List<Delivery> deliveries) {
		obsList = FXCollections.observableArrayList(deliveries);
		tableViewBusca.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
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
				btEditar.setPrefHeight(22.0);
				btEditar.setPrefWidth(75.0);
				btEditar.setFont(new Font("Arial", 12));
				btEditar.setOnAction(event -> loadView(obj));
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

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Delivery, Delivery>() {
			private final Button btRemover = new Button("Remover");

			@Override
			protected void updateItem(Delivery obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(btRemover);
				btRemover.setPrefHeight(22.0);
				btRemover.setPrefWidth(75.0);
				btRemover.setFont(new Font("Arial", 12));
				btRemover.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Delivery obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Tem certeza que você quer remover?");

		if (result.get() == ButtonType.OK) {
			if (deliveryService == null || localizationService == null || consigneeService == null
					|| senderService == null) {
				throw new IllegalStateException("Service was null");
			}

			try {
				deliveryService.remove(obj);
				localizationService.remove(obj.getLocalization());
				consigneeService.remove(obj.getConsignee());
				senderService.remove(obj.getSender());

				updateTableView(deliveryService.findAll());
			} catch (DbIntegrityException e) {
				Alerts.showAlert("RapidaEntrega", null, "Erro ao remover no Banco de Dados", AlertType.ERROR);
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		comboBoxFiltro.setItems(
				FXCollections.observableArrayList("Todos", "Destinatário", "Remetente", "País", "Estado", "Cidade"));
		comboBoxFiltro.getSelectionModel().selectFirst();

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
