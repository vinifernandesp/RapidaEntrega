package model.dao;

import java.util.List;

import model.entities.Consignee;
import model.entities.LetterDelivery;
import model.entities.Localization;
import model.entities.Sender;

public interface LetterDeliveryDao {

	void insert(LetterDelivery obj);
	void update(LetterDelivery obj);
	void deleteById(Integer id);
	LetterDelivery findById(Integer id);
	List<LetterDelivery> findAll();
	List<LetterDelivery> findBySender(Sender sender);
	List<LetterDelivery> findByConsignee(Consignee consignee);
	List<LetterDelivery> findByLocalization(Localization localization);
}
