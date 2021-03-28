package model.entities;

import model.enumerator.TypeOfDelivery;

public class PackageDelivery extends Delivery {

	private static final long serialVersionUID = 1L;

	private Double weight;

	public PackageDelivery() {
		super();
	}
	
	public PackageDelivery(Integer id, Localization localization, Consignee consignee, Sender sender, double weight) {
		super(id, localization, consignee, sender);
		super.setTypeOfDelivery(TypeOfDelivery.PACKAGE);
		this.weight = weight;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}
