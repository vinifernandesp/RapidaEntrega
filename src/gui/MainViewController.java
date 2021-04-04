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
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.entities.Consignee;
import model.entities.LetterDelivery;
import model.entities.Localization;
import model.entities.PackageDelivery;
import model.entities.Sender;
import model.service.ConsigneeService;
import model.service.DeliveryService;
import model.service.LocalizationService;
import model.service.SenderService;

public class MainViewController implements Initializable {

	@FXML
	private Button btnCadastro;
	
	@FXML
	private Button btnBusca;
	
	@FXML
	private Button btnSobre;
	
	@FXML
	public void onBtnCadastroAction() {
		loadView("/gui/Cadastro.fxml", (CadastroController controller) -> {
			controller.setSender(new Sender());
			controller.setConsignee(new Consignee());
			controller.setLocalization(new Localization());
			controller.setPackageDelivery(new PackageDelivery());
			controller.setLetterDelivery(new LetterDelivery());
			controller.setSenderService(new SenderService());
			controller.setConsigneeService(new ConsigneeService());
			controller.setLocalizationService(new LocalizationService());
			controller.setDeliveryService(new DeliveryService());
		});
	}
	
	@FXML
	public void onBtnBuscaAction() {
		loadView("/gui/Busca.fxml", (BuscaController controller) -> {
			controller.setDeliveryService(new DeliveryService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onBtnSobreAction() {
		loadView("/gui/Sobre.fxml", x -> {});
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			AnchorPane newPage = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			mainVBox.getChildren().set(1, newPage);
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("RapidaEntrega", null, "Erro de carregamento de Página", AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
}
