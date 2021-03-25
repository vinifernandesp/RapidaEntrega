package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entities.Sender;

public class SenderService {

	public List<Sender> findAll() {
		List<Sender> senders = new ArrayList<>();
		senders.add(new Sender(1, "Magazine Luíza"));
		senders.add(new Sender(2, "Casas Bahia"));
		senders.add(new Sender(3, "Samsung Brasil"));
		
		return senders;
	}
}
