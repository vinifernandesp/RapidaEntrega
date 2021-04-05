package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.LocalizationDao;
import model.entities.Delivery;
import model.entities.Localization;

public class LocalizationService {

	private LocalizationDao dao = DaoFactory.createLocalizationDao();
	
	public List<Localization> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Localization obj) {
		if (obj.getId() == null) {
			List<Localization> localizations = dao.findAll();
			Localization search = localizations.stream()
					.filter(x -> x.getCountry().compareTo(obj.getCountry()) == 0 && 
					x.getState().compareTo(obj.getState()) == 0 &&
					x.getCity().compareTo(obj.getCity()) == 0)
					.findFirst().orElse(null);

			if (search == null) dao.insert(obj);
		}
		
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Localization obj) {
		List<Delivery> allDeliveries = new DeliveryService().findAll();

		if (allDeliveries.stream().filter(x -> x.getLocalization().getId().compareTo(obj.getId()) == 0).findFirst()
				.orElse(null) == null) {
			dao.deleteById(obj.getId());
		}
	}
}
