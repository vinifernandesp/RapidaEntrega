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
}
