package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entities.Consignee;
import model.enumerator.TypeOfPersonIdentifier;

public class ConsigneeService {

	public List<Consignee> findAll() {
		List<Consignee> consignees = new ArrayList<>();
		consignees.add(new Consignee(1, "João", "123.456.789-00", TypeOfPersonIdentifier.CPF));
		consignees.add(new Consignee(2, "Maria", "123.456.789-01", TypeOfPersonIdentifier.CPF));
		consignees.add(new Consignee(3, "Philipe", "123.456.789-02", TypeOfPersonIdentifier.CPF));
		
		return consignees;
	}
}
