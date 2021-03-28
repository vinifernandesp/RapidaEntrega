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
}
