package model.dao;

import java.util.List;

import model.entities.Localization;

public interface LocalizationDao {

	void insert(Localization obj);
	void update(Localization obj);
	void deleteById(Integer id);
	Localization findById(Integer id);
	List<Localization> findAll();
}
