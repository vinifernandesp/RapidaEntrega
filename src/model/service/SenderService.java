package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SenderDao;
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

			if (search == null) dao.insert(obj);
		}

		else {
			dao.update(obj);
		}
	}
}
