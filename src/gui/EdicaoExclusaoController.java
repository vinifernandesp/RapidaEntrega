package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class EdicaoExclusaoController implements Initializable{

	@FXML
	private TableView<String> tableViewEdicaoExclusao;
	
	@FXML
	private TableColumn<String, Integer> tableColumnId;
	
	@FXML
	private TableColumn<String, String> tableColumnDestinatario;
	
	@FXML
	private TableColumn<String, String> tableColumnRemetente;
	
	@FXML
	private TableColumn<String, Integer> tableColumnIdEndereco;
	
	@FXML
	private TableColumn<String, String> tableColumnEndereco;
	
	@FXML
	private Button btEditar;
	
	@FXML
	private Button btExcluir;
	
	@FXML
	public void onBtEditarAction() {
		System.out.println("onBtEditarAction");
	}
	
	@FXML
	public void onBtExcluirAction() {
		System.out.println("onBtExcluirAction");	
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
}
