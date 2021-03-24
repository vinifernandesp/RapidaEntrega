package model.entities;

import model.enumerator.TypeOfDelivery;

public class LetterDelivery extends Delivery{

	private static final long serialVersionUID = 1L;

	private Boolean envelope;
	
	public LetterDelivery() {
		super();
	}

	public LetterDelivery(Integer id, Localization localization, Consignee consignee, Sender sender, boolean envelope) {
		super(id, localization, consignee, sender);
		super.setTypeOfDelivery(TypeOfDelivery.LETTER);
		this.envelope = envelope;
	}

	public boolean isEnvelope() {
		return envelope;
	}

	public void setEnvelope(boolean envelope) {
		this.envelope = envelope;
	}
}
