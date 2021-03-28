package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.LocalizationDao;
import model.entities.Localization;

public class LocalizationService {

	private LocalizationDao dao = DaoFactory.createLocalizationDao();
	
	public List<Localization> findAll() {
		return dao.findAll();
	}
}
