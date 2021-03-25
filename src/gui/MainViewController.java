package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
	private Button btnEdicao;
	
	@FXML
	private Button btnSobre;
	
	@FXML
	public void onBtnCadastroAction() {
		loadView("/gui/Cadastro.fxml");
	}
	
	@FXML
	public void onBtnBuscaAction() {
		loadViewBusca("/gui/Busca.fxml");
	}

	@FXML
	public void onBtnEdicaoAction() {
		loadViewEdicaoExclusao("/gui/EdicaoExclusao.fxml");
	}

	@FXML
	public void onBtnSobreAction() {
		loadView("/gui/Sobre.fxml");
	}
	
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			AnchorPane newPage = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			mainVBox.getChildren().set(1, newPage);
		} catch (IOException e) {
			Alerts.showAlert("Error", null, "Erro de carregamento de Página", AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	private void loadViewBusca(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			AnchorPane newPage = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			mainVBox.getChildren().set(1, newPage);
			
			BuscaController controller = loader.getController();
			controller.setDeliveryService(new DeliveryService(new LocalizationService(), new ConsigneeService(), new SenderService()));
			controller.updateTableView();
		} catch (IOException e) {
			Alerts.showAlert("Error", null, "Erro de carregamento de Página", AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	private void loadViewEdicaoExclusao(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			AnchorPane newPage = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			mainVBox.getChildren().set(1, newPage);
			
			EdicaoExclusaoController controller = loader.getController();
			controller.setDeliveryService(new DeliveryService(new LocalizationService(), new ConsigneeService(), new SenderService()));
			controller.updateTableView();
		} catch (IOException e) {
			Alerts.showAlert("Error", null, "Erro de carregamento de Página", AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
}
