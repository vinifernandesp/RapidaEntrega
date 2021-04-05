package model.service;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.LetterDeliveryDao;
import model.dao.PackageDeliveryDao;
import model.entities.Consignee;
import model.entities.Delivery;
import model.entities.LetterDelivery;
import model.entities.Localization;
import model.entities.PackageDelivery;
import model.entities.Sender;

public class DeliveryService {

	private PackageDeliveryDao daoPackage = DaoFactory.createPackageDeliveryDao();
	private LetterDeliveryDao daoLetter = DaoFactory.createLetterDeliveryDao();

	public List<Delivery> findAll() {
		List<Delivery> allDeliveries = new ArrayList<>();
		List<PackageDelivery> packageDeliveries = daoPackage.findAll();
		List<LetterDelivery> letterDeliveries = daoLetter.findAll();

		for (Delivery deliveries : packageDeliveries) {
			allDeliveries.add(deliveries);
		}

		for (Delivery deliveries : letterDeliveries) {
			allDeliveries.add(deliveries);
		}

		allDeliveries.sort((p1, p2) -> p1.getConsignee().getId().compareTo(p2.getConsignee().getId()));
		
		return allDeliveries;
	}

	public void saveOrUpdate(Delivery obj) {
		if (obj instanceof PackageDelivery) {
			PackageDelivery packageDelivery = (PackageDelivery) obj;
		
			if (packageDelivery.getId() == null) {
				saveObjects(packageDelivery);
				daoPackage.insert(packageDelivery);
			}
			else {
				daoPackage.update(packageDelivery);
			}
		}
		
		else if (obj instanceof LetterDelivery) {
			LetterDelivery letterDelivery = (LetterDelivery) obj;
			
			if (letterDelivery.getId() == null) {
				saveObjects(letterDelivery);
				daoLetter.insert(letterDelivery);
			}
			else {
				daoLetter.update(letterDelivery);
			}
		}
	}
	
	private void saveObjects(Delivery obj) {
		List<Sender> senders = new SenderService().findAll();
		obj.getSender().setId(senders.stream()
				.filter(x -> x.getName().compareTo(obj.getSender().getName()) == 0).findFirst()
				.get().getId());

		List<Consignee> consignees = new ConsigneeService().findAll();
		obj.getConsignee().setId(consignees.stream()
				.filter(x -> x.getName().compareTo(obj.getConsignee().getName()) == 0 && 
				x.getPersonIdentifier().compareTo(obj.getConsignee().getPersonIdentifier()) == 0).findFirst()
				.get().getId());

		List<Localization> localizations = new LocalizationService().findAll();
		obj.getLocalization().setId(localizations.stream()
				.filter(x -> x.getCountry().compareTo(obj.getLocalization().getCountry()) == 0 && 
				x.getState().compareTo(obj.getLocalization().getState()) == 0 && 
				x.getCity().compareTo(obj.getLocalization().getCity()) == 0).findFirst()
				.get().getId());
	}
	
	public void remove(Delivery obj) {
		if (obj instanceof PackageDelivery) {
			PackageDelivery packageDelivery = (PackageDelivery) obj;
		
			daoPackage.deleteById(packageDelivery.getId());
		}
		
		else if (obj instanceof LetterDelivery) {
			LetterDelivery letterDelivery = (LetterDelivery) obj;
			
			daoLetter.deleteById(letterDelivery.getId());
		}
	}
}
