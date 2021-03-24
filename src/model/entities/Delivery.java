package model.entities;

import java.io.Serializable;

import model.enumerator.TypeOfDelivery;

public abstract class Delivery implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Localization localization;
	private Consignee consignee;
	private Sender sender;
	private TypeOfDelivery typeOfDelivery;
	
	public Delivery() {
	}

	public Delivery(Integer id, Localization localization, Consignee consignee, Sender sender) {
		this.id = id;
		this.localization = localization;
		this.consignee = consignee;
		this.sender = sender;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Localization getLocalization() {
		return localization;
	}

	public void setLocalization(Localization localization) {
		this.localization = localization;
	}

	public Consignee getConsignee() {
		return consignee;
	}

	public void setConsignee(Consignee consignee) {
		this.consignee = consignee;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public TypeOfDelivery getTypeOfDelivery() {
		return typeOfDelivery;
	}

	public void setTypeOfDelivery(TypeOfDelivery typeOfDelivery) {
		this.typeOfDelivery = typeOfDelivery;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Delivery other = (Delivery) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
