package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BuscaController implements Initializable{

	@FXML
	private ComboBox<String> comboBoxFiltro;
	
	@FXML
	private TextField txtPalavraFiltro;
	
	@FXML
	private Button btFiltro;
	
	@FXML
	private TableView<String> tableViewBusca;
	
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
	public void onComboBoxFiltroAction() {
		System.out.println("onComboBoxFiltroAction");
	}
	
	@FXML
	public void onBtFiltroAction() {
		System.out.println("onBtFiltroAction");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {	
	}
}
