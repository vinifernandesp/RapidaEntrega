package model.dao;

import java.util.List;

import model.entities.Sender;

public interface SenderDao {

	void insert(Sender obj);
	void update(Sender obj);
	void deleteById(Integer id);
	Sender findById(Integer id);
	List<Sender> findAll();
}
