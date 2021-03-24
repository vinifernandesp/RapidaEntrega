package model.entities;

import java.io.Serializable;

import model.enumerator.TypeOfPersonIdentifier;

public class Consignee implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String personIdentifier;
	private TypeOfPersonIdentifier typeOfPersonIdentifier;
	
	public Consignee() {		
	}

	public Consignee(Integer id, String name, String personIdentifier, TypeOfPersonIdentifier typeOfPersonIdentifier) {
		this.id = id;
		this.name = name;
		this.personIdentifier = personIdentifier;
		this.typeOfPersonIdentifier = typeOfPersonIdentifier;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonIdentifier() {
		return personIdentifier;
	}

	public void setPersonIdentifier(String personIdentifier) {
		this.personIdentifier = personIdentifier;
	}

	public TypeOfPersonIdentifier getTypeOfPersonIdentifier() {
		return typeOfPersonIdentifier;
	}

	public void setTypeOfPersonIdentifier(TypeOfPersonIdentifier typeOfPersonIdentifier) {
		this.typeOfPersonIdentifier = typeOfPersonIdentifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((personIdentifier == null) ? 0 : personIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Consignee other = (Consignee) obj;
		if (personIdentifier == null) {
			if (other.personIdentifier != null)
				return false;
		} else if (!personIdentifier.equals(other.personIdentifier))
			return false;
		return true;
	}
}
