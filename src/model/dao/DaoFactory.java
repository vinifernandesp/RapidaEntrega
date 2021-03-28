package model.dao;

import db.DB;
import model.dao.impl.ConsigneeDaoJDBC;
import model.dao.impl.LetterDeliveryDaoJDBC;
import model.dao.impl.LocalizationDaoJDBC;
import model.dao.impl.PackageDeliveryDaoJDBC;
import model.dao.impl.SenderDaoJDBC;

public class DaoFactory {

	public static SenderDao createSenderDao() {
		return new SenderDaoJDBC(DB.getConnection());
	}

	public static ConsigneeDao createConsigneeDao() {
		return new ConsigneeDaoJDBC(DB.getConnection());
	}

	public static LocalizationDao createLocalizationDao() {
		return new LocalizationDaoJDBC(DB.getConnection());
	}

	public static PackageDeliveryDao createPackageDeliveryDao() {
		return new PackageDeliveryDaoJDBC(DB.getConnection());
	}

	public static LetterDeliveryDao createLetterDeliveryDao() {
		return new LetterDeliveryDaoJDBC(DB.getConnection());
	}
}
