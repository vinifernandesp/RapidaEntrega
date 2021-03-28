package model.service;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.LetterDeliveryDao;
import model.dao.PackageDeliveryDao;
import model.entities.Delivery;
import model.entities.LetterDelivery;
import model.entities.PackageDelivery;

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
		
		return allDeliveries;
	}
}
