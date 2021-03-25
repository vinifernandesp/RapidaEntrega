package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entities.Consignee;
import model.entities.Delivery;
import model.entities.LetterDelivery;
import model.entities.Localization;
import model.entities.PackageDelivery;
import model.entities.Sender;

public class DeliveryService {

	private LocalizationService localizationService;
	private ConsigneeService consigneeService;
	private SenderService sendersService;
	
	public DeliveryService(LocalizationService localizationService, ConsigneeService consigneeService, 
			SenderService sendersService) {
		this.localizationService = localizationService;
		this.consigneeService = consigneeService;
		this.sendersService = sendersService;
	}
	
	public List<Delivery> findAll() {
		List<Localization> locations = localizationService.findAll();
		List<Consignee> consignees = consigneeService.findAll();
		List<Sender> senders = sendersService.findAll();
		
		List<Delivery> deliveries = new ArrayList<>();
		deliveries.add(new PackageDelivery(1, locations.get(0), consignees.get(0), senders.get(0), 10.5));
		deliveries.add(new PackageDelivery(2, locations.get(1), consignees.get(1), senders.get(1), 4.45));
		deliveries.add(new LetterDelivery(3, locations.get(2), consignees.get(2), senders.get(2), false));
		
		return deliveries;
	}
}
