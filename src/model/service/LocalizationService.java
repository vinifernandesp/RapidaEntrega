package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entities.Localization;

public class LocalizationService {

	public List<Localization> findAll() {
		List<Localization> locations = new ArrayList<>();
		locations.add(new Localization(1, "BRA", "MT", "Sinop"));
		locations.add(new Localization(2, "BRA", "DF", "Brasília"));
		locations.add(new Localization(3, "BRA", "PR", "Curitiba"));
		
		return locations;
	}
}
