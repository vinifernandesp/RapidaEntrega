package model.dao;

import java.util.List;

import model.entities.Consignee;

public interface ConsigneeDao {

	void insert(Consignee obj);
	void update(Consignee obj);
	void deleteById(Integer id);
	Consignee findById(Integer id);
	List<Consignee> findAll();
}
