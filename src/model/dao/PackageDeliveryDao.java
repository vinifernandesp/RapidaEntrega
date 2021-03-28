package model.dao;

import java.util.List;

import model.entities.Consignee;
import model.entities.Localization;
import model.entities.PackageDelivery;
import model.entities.Sender;

public interface PackageDeliveryDao {

	void insert(PackageDelivery obj);
	void update(PackageDelivery obj);
	void deleteById(Integer id);
	PackageDelivery findById(Integer id);
	List<PackageDelivery> findAll();
	List<PackageDelivery> findBySender(Sender sender);
	List<PackageDelivery> findByConsignee(Consignee consignee);
	List<PackageDelivery> findByLocalization(Localization localization);
}
