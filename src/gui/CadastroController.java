package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class CadastroController implements Initializable{

	@FXML
	private TextField txtPais;
	
	@FXML
	private TextField txtEstado;
	
	@FXML
	private TextField txtCidade;
	
	@FXML
	private TextField txtDestinatario;
	
	@FXML
	private TextField txtRemetente;
	
	@FXML
	private RadioButton rbCPF;
	
	@FXML
	private RadioButton rbCNPJ;
	
	@FXML
	private TextField txtCPF_CNPJ;
	
	@FXML
	private RadioButton rbCarta;
	
	@FXML
	private RadioButton rbEnvelope;
	
	@FXML
	private RadioButton rbPacote;
	
	@FXML
	private Spinner<Double> spinnerPeso;
	
	@FXML
	private Button btLimpar;
	
	@FXML
	private Button btSalvar;
	
	ToggleGroup groupCPF_CNPJ = new ToggleGroup();
	
	ToggleGroup groupCartaPacote = new ToggleGroup();
	
	@FXML
	public void onRbCPFAction() {
		System.out.println("onRbCPFAction");
	}
	
	@FXML
	public void onRbCNPJAction() {
		System.out.println("onRbCNPJAction");
	}
	
	@FXML
	public void onRbCartaAction() {
		System.out.println("onRbCartaAction");
	}
	
	@FXML
	public void onRbPacoteAction() {
		System.out.println("onRbPacoteAction");
	}
	
	@FXML
	public void onBtLimparAction() {
		System.out.println("onBtLimparAction");
	}
	
	@FXML
	public void onBtSalvarAction() {
		System.out.println("onBtSalvarAction");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
}
