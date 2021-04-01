package model.service;

import java.util.List;

import model.dao.ConsigneeDao;
import model.dao.DaoFactory;
import model.entities.Consignee;

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
}
