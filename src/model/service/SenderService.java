package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SenderDao;
import model.entities.Delivery;
import model.entities.Sender;

public class SenderService {

	private SenderDao dao = DaoFactory.createSenderDao();

	public List<Sender> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Sender obj) {
		if (obj.getId() == null) {
			List<Sender> senders = dao.findAll();
			Sender search = senders.stream().filter(x -> x.getName().compareTo(obj.getName()) == 0).findFirst()
					.orElse(null);

			if (search == null)
				dao.insert(obj);
		}

		else {
			dao.update(obj);
		}
	}

	public void remove(Sender obj) {
		List<Delivery> allDeliveries = new DeliveryService().findAll();

		if (allDeliveries.stream().filter(x -> x.getSender().getId().compareTo(obj.getId()) == 0).findFirst()
				.orElse(null) == null) {
			dao.deleteById(obj.getId());
		}
	}
}
