package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
		System.out.println("onMenuCadastroAction");
	}
	
	@FXML
	public void onBtnBuscaAction() {
		System.out.println("onMenuBuscaAction");
	}
	
	@FXML
	public void onBtnEdicaoAction() {
		System.out.println("onMenuEdicaoAction");
	}
	
	@FXML
	public void onBtnSobreAction() {
		System.out.println("onMenuSobreAction");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
}
