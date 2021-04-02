package gui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.MaskFieldUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.entities.Consignee;
import model.entities.Delivery;
import model.entities.LetterDelivery;
import model.entities.Localization;
import model.entities.PackageDelivery;
import model.entities.Sender;
import model.enumerator.TypeOfDelivery;
import model.enumerator.TypeOfPersonIdentifier;
import model.exceptions.ValidationException;
import model.service.ConsigneeService;
import model.service.DeliveryService;
import model.service.LocalizationService;
import model.service.SenderService;

public class CadastroController implements Initializable{

	private Sender sender;
	private Consignee consignee;
	private Localization localization;
	private Delivery delivery;
	private PackageDelivery packageDelivery;
	private LetterDelivery letterDelivery;
	
	private SenderService senderService;
	private ConsigneeService consigneeService;
	private LocalizationService localizationService;
	private DeliveryService deliveryService;
	
	@FXML
	private TextField txtPais;
	
	@FXML
	private Label labelPaisError;
	
	@FXML
	private TextField txtEstado;
	
	@FXML
	private Label labelEstadoError;
	
	@FXML
	private TextField txtCidade;
	
	@FXML
	private Label labelCidadeError;
	
	@FXML
	private TextField txtDestinatario;
	
	@FXML
	private Label labelDestinatarioError;
	
	@FXML
	private TextField txtRemetente;
	
	@FXML
	private Label labelRemetenteError;
	
	@FXML
	private RadioButton rbCPF;
	
	@FXML
	private RadioButton rbCNPJ;
	
	@FXML
	private TextField txtCPF_CNPJ;
	
	@FXML
	private Label labelCPF_CNPJError;
	
	@FXML
	private RadioButton rbCarta;
	
	@FXML
	private RadioButton rbEnvelope;
	
	@FXML
	private RadioButton rbPacote;
	
	@FXML
	private Spinner<Double> spinnerPeso;
	
	@FXML
	private Label labelKg;
	
	@FXML
	private Label labelCartaPacoteError;
	
	@FXML
	private Button btLimpar;
	
	@FXML
	private Button btSalvar;
	
	private SpinnerValueFactory<Double> spinnerValue;
	private ToggleGroup groupCPF_CNPJ;
	private ToggleGroup groupCartaPacote;
	
	private ValidationException exception;

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public void setConsignee(Consignee consignee) {
		this.consignee = consignee;
	}
	
	public void setLocalization(Localization localization) {
		this.localization = localization;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

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
	public void onRbCPFAction() {
		txtCPF_CNPJ.setEditable(true);
		txtCPF_CNPJ.setText("");
		
		MaskFieldUtil.maskCPF(txtCPF_CNPJ);
		Constraints.setTextFieldMaxLength(txtCPF_CNPJ, 14);
	}
	
	@FXML
	public void onRbCNPJAction() {
		txtCPF_CNPJ.setEditable(true);
		txtCPF_CNPJ.setText("");
		
		MaskFieldUtil.maskCNPJ(txtCPF_CNPJ);
		Constraints.setTextFieldMaxLength(txtCPF_CNPJ, 18);
	}
	
	@FXML
	public void onRbCartaAction() {
		rbEnvelope.setVisible(true);
		spinnerPeso.setVisible(false);
		labelKg.setVisible(false);
	}
	
	@FXML
	public void onRbPacoteAction() {
		rbEnvelope.setVisible(false);
		spinnerPeso.setVisible(true);
		labelKg.setVisible(true);
	}
	
	@FXML
	public void onBtLimparAction() {
		cleanAll();
	}
	
	@FXML
	public void onBtSalvarAction() {
		try {
			cleanAllErrorsMessages(exception.getErrors());
			
			sender = getFormDataSender();
			consignee = getFormDataConsignee();
			localization = getFormDataLocalization();
			
			if (rbPacote.isSelected()) {
				delivery = getFormDataPackage();
			}
			else if (rbCarta.isSelected()) {
				delivery = getFormDataLetterDelivery();
			}
			else {
				exception.addError("packageLetter", " Selecione o tipo de entrega");
			}
			
			if(exception.getErrors().size() > 0) throw exception;
			
			senderService.saveOrUpdate(sender);
			consigneeService.saveOrUpdate(consignee);
			localizationService.saveOrUpdate(localization);
			deliveryService.saveOrUpdate(delivery);
			
			cleanAll();
			Alerts.showAlert("RapidaEntrega", null, "Salvo com Sucesso!", AlertType.INFORMATION);
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("RapidaEntrega", null, "Erro ao salvar no Banco de Dados", AlertType.ERROR);
		}
	}

	private void cleanAll() {
		txtPais.setText("");
		txtEstado.setText("");
		txtCidade.setText("");
		txtDestinatario.setText("");
		txtRemetente.setText("");
		txtCPF_CNPJ.setText("");
		rbCPF.setSelected(false);
		rbCNPJ.setSelected(false);
		rbCarta.setSelected(false);
		rbPacote.setSelected(false);
		rbEnvelope.setSelected(false);
		rbEnvelope.setVisible(false);
		spinnerPeso.setValueFactory(spinnerValue);
		spinnerPeso.setVisible(false);
		labelKg.setVisible(false);
	}

	private Sender getFormDataSender() {
		Sender obj = new Sender();
		
		if (txtRemetente.getText() == null || txtRemetente.getText().trim().equals("")) {
			exception.addError("sender", " Preencha o campo");
		}
		obj.setName(txtRemetente.getText());
		
		return obj;
	}
	
	private Consignee getFormDataConsignee() {
		Consignee obj = new Consignee();
		
		if (txtDestinatario.getText() == null || txtDestinatario.getText().trim().equals("")) {
			exception.addError("consignee", " Preencha o campo");
		}
		obj.setName(txtDestinatario.getText());
		
		if (txtCPF_CNPJ.getText() == null || txtCPF_CNPJ.getText().trim().equals("")) {
			exception.addError("personIdentifier", " Preencha o campo");
		}
		obj.setPersonIdentifier(txtCPF_CNPJ.getText());
		
		if (rbCPF.isSelected()) obj.setTypeOfPersonIdentifier(TypeOfPersonIdentifier.CPF);
		else if(rbCNPJ.isSelected()) obj.setTypeOfPersonIdentifier(TypeOfPersonIdentifier.CNPJ);
		else {
			exception.addError("typeOfPersonIdentifier", " Selecione o tipo de pessoa");
		}
		
		return obj;
	}
	
	private Localization getFormDataLocalization() {
		Localization obj = new Localization();
		
		if (txtPais.getText() == null || txtPais.getText().trim().equals("")) {
			exception.addError("country", " Preencha o campo");
		}
		obj.setCountry(txtPais.getText());
		
		if (txtEstado.getText() == null || txtEstado.getText().trim().equals("")) {
			exception.addError("state", " Preencha o campo");
		}
		obj.setState(txtEstado.getText());
		
		if (txtCidade.getText() == null || txtCidade.getText().trim().equals("")) {
			exception.addError("city", " Preencha o campo");
		}
		obj.setCity(txtCidade.getText());
		
		return obj;
	}
	
	private PackageDelivery getFormDataPackage() {
		PackageDelivery obj = new PackageDelivery();
		
		if (spinnerPeso.getValue().compareTo(0.0) == 0 || spinnerPeso.getValue().equals(null)) {
			exception.addError("packageLetter", " Adicione um peso");
		}
		obj.setWeight(spinnerPeso.getValue());
		obj.setSender(sender);
		obj.setConsignee(consignee);
		obj.setLocalization(localization);
		obj.setTypeOfDelivery(TypeOfDelivery.PACKAGE);
		
		return obj;
	}
	
	private LetterDelivery getFormDataLetterDelivery() {
		LetterDelivery obj = new LetterDelivery();
		obj.setEnvelope(rbEnvelope.isSelected());
		obj.setSender(sender);
		obj.setConsignee(consignee);
		obj.setLocalization(localization);
		obj.setTypeOfDelivery(TypeOfDelivery.LETTER);
		
		return obj;
	}
	
	public void updateFormData() {
		if (sender == null || consignee == null || localization == null || delivery == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtPais.setText(localization.getCountry());
		txtEstado.setText(localization.getState());
		txtCidade.setText(localization.getCity());
		txtDestinatario.setText(consignee.getName());
		txtCPF_CNPJ.setText(consignee.getPersonIdentifier());
		
		if (consignee.getTypeOfPersonIdentifier() == TypeOfPersonIdentifier.CPF) {
			rbCPF.setSelected(true);
		}
		else if (consignee.getTypeOfPersonIdentifier() == TypeOfPersonIdentifier.CNPJ) {
			rbCNPJ.setSelected(true);
		}
		
		if (delivery instanceof PackageDelivery) {
			packageDelivery = (PackageDelivery) delivery;
			
			rbPacote.setSelected(true);
			spinnerPeso.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(packageDelivery.getWeight(), 100, 0));
		}
		
		else if (delivery instanceof LetterDelivery) {
			letterDelivery = (LetterDelivery) delivery;
			
			rbCarta.setSelected(true);
			if (letterDelivery.isEnvelope()) {
				rbEnvelope.setSelected(true);
			}
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("sender")) labelRemetenteError.setText(errors.get("sender"));
		if (fields.contains("consignee")) labelDestinatarioError.setText(errors.get("consignee"));
		if (fields.contains("personIdentifier")) labelCPF_CNPJError.setText(errors.get("personIdentifier"));
		if (fields.contains("typeOfPersonIdentifier")) labelCPF_CNPJError.setText(errors.get("typeOfPersonIdentifier"));
		if (fields.contains("country")) labelPaisError.setText(errors.get("country"));
		if (fields.contains("state")) labelEstadoError.setText(errors.get("state"));
		if (fields.contains("city")) labelCidadeError.setText(errors.get("city"));
		if (fields.contains("packageLetter")) labelCartaPacoteError.setText(errors.get("packageLetter"));
	}
	
	private void cleanAllErrorsMessages(Map<String, String> errors) {
		errors.clear();
		
		labelRemetenteError.setText("");
		labelDestinatarioError.setText("");
		labelCPF_CNPJError.setText("");
		labelCPF_CNPJError.setText("");
		labelPaisError.setText("");
		labelEstadoError.setText("");
		labelCidadeError.setText("");
		labelCartaPacoteError.setText("");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtCidade, 15);
		Constraints.setTextFieldMaxLength(txtEstado, 15);
		Constraints.setTextFieldMaxLength(txtPais, 15);
		Constraints.setTextFieldMaxLength(txtDestinatario, 30);
		Constraints.setTextFieldMaxLength(txtRemetente, 30);
		
		groupCPF_CNPJ = new ToggleGroup();
		rbCPF.setToggleGroup(groupCPF_CNPJ);
		rbCNPJ.setToggleGroup(groupCPF_CNPJ);
		
		groupCartaPacote = new ToggleGroup();
		rbCarta.setToggleGroup(groupCartaPacote);
		rbPacote.setToggleGroup(groupCartaPacote);
		
		spinnerValue = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 0);
		spinnerPeso.setValueFactory(spinnerValue);
		
		exception = new ValidationException("Validation error");
	}
}
