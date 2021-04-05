package model.service;

import java.util.List;

import model.dao.ConsigneeDao;
import model.dao.DaoFactory;
import model.entities.Consignee;
import model.entities.Delivery;

public class ConsigneeService {

	private ConsigneeDao dao = DaoFactory.createConsigneeDao();

	public List<Consignee> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Consignee obj) {
		if (obj.getId() == null) {
			List<Consignee> consignees = dao.findAll();
			Consignee search = consignees.stream()
					.filter(x -> x.getName().compareTo(obj.getName()) == 0 && 
					x.getPersonIdentifier().compareTo(obj.getPersonIdentifier()) == 0)
					.findFirst().orElse(null);

			if (search == null) dao.insert(obj);
		}

		else {
			dao.update(obj);
		}
	}
	
	public void remove(Consignee obj) {
		List<Delivery> allDeliveries = new DeliveryService().findAll();

		if (allDeliveries.stream().filter(x -> x.getConsignee().getId().compareTo(obj.getId()) == 0).findFirst()
				.orElse(null) == null) {
			dao.deleteById(obj.getId());
		}
	}
}
