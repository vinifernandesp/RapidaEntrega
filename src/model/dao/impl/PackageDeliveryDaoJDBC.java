package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.PackageDeliveryDao;
import model.entities.Consignee;
import model.entities.Localization;
import model.entities.PackageDelivery;
import model.entities.Sender;
import model.enumerator.TypeOfPersonIdentifier;

public class PackageDeliveryDaoJDBC implements PackageDeliveryDao {

	private Connection conn;

	public PackageDeliveryDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(PackageDelivery obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO tb_package_delivery "
					+ "(weight, id_localization, id_consignee, id_sender) " 
					+ "VALUES " 
					+ "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setDouble(1, obj.getWeight());
			st.setInt(2, obj.getLocalization().getId());
			st.setInt(3, obj.getConsignee().getId());
			st.setInt(4, obj.getSender().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(PackageDelivery obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE tb_package_delivery "
					+ "SET weight = ?, id_localization = ?, id_consignee = ?, id_sender = ? "
					+ "WHERE id_package_delivery = ?");

			st.setDouble(1, obj.getWeight());
			st.setInt(2, obj.getLocalization().getId());
			st.setInt(3, obj.getConsignee().getId());
			st.setInt(4, obj.getSender().getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM tb_package_delivery WHERE id_package_delivery = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public PackageDelivery findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT tb_package_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_package_delivery "
					+ "INNER JOIN tb_sender ON tb_package_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_package_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_package_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE tb_package_delivery.id_package_delivery = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Sender sender = instantiateSender(rs);
				Consignee consignee = instantiateConsignee(rs);
				Localization localization = instantiateLocalization(rs);
				PackageDelivery obj = instantiatePackageDelivery(rs, sender, consignee, localization);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<PackageDelivery> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_package_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_package_delivery "
					+ "INNER JOIN tb_sender ON tb_package_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_package_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_package_delivery.id_localization = tb_localization.id_localization");

			rs = st.executeQuery();

			List<PackageDelivery> list = new ArrayList<>();
			Map<Integer, Sender> mapSender = new HashMap<>();
			Map<Integer, Consignee> mapConsignee = new HashMap<>();
			Map<Integer, Localization> mapLocalization = new HashMap<>();

			while (rs.next()) {

				Sender searchSender = mapSender.get(rs.getInt("id_sender"));
				if (searchSender == null) {
					searchSender = instantiateSender(rs);
					mapSender.put(rs.getInt("id_sender"), searchSender);
				}

				Consignee searchConsignee = mapConsignee.get(rs.getInt("id_consignee"));
				if (searchConsignee == null) {
					searchConsignee = instantiateConsignee(rs);
					mapConsignee.put(rs.getInt("id_consignee"), searchConsignee);
				}

				Localization searchLocalization = mapLocalization.get(rs.getInt("id_localization"));
				if (searchLocalization == null) {
					searchLocalization = instantiateLocalization(rs);
					mapLocalization.put(rs.getInt("id_localization"), searchLocalization);
				}

				PackageDelivery obj = instantiatePackageDelivery(rs, searchSender, searchConsignee, searchLocalization);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<PackageDelivery> findBySender(Sender sender) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_package_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_package_delivery "
					+ "INNER JOIN tb_sender ON tb_package_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_package_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_package_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE id_sender = ? " 
					+ "ORDER BY tb_sender.name");

			st.setInt(1, sender.getId());

			rs = st.executeQuery();

			List<PackageDelivery> list = new ArrayList<>();
			Map<Integer, Sender> mapSender = new HashMap<>();
			Map<Integer, Consignee> mapConsignee = new HashMap<>();
			Map<Integer, Localization> mapLocalization = new HashMap<>();

			while (rs.next()) {

				Sender searchSender = mapSender.get(rs.getInt("id_sender"));
				if (searchSender == null) {
					searchSender = instantiateSender(rs);
					mapSender.put(rs.getInt("id_sender"), searchSender);
				}

				Consignee searchConsignee = mapConsignee.get(rs.getInt("id_consignee"));
				if (searchConsignee == null) {
					searchConsignee = instantiateConsignee(rs);
					mapConsignee.put(rs.getInt("id_consignee"), searchConsignee);
				}

				Localization searchLocalization = mapLocalization.get(rs.getInt("id_localization"));
				if (searchLocalization == null) {
					searchLocalization = instantiateLocalization(rs);
					mapLocalization.put(rs.getInt("id_localization"), searchLocalization);
				}

				PackageDelivery obj = instantiatePackageDelivery(rs, searchSender, searchConsignee, searchLocalization);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<PackageDelivery> findByConsignee(Consignee consignee) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_package_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_package_delivery "
					+ "INNER JOIN tb_sender ON tb_package_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_package_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_package_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE id_consignee = ? " 
					+ "ORDER BY tb_consignee.name");

			st.setInt(1, consignee.getId());

			rs = st.executeQuery();

			List<PackageDelivery> list = new ArrayList<>();
			Map<Integer, Sender> mapSender = new HashMap<>();
			Map<Integer, Consignee> mapConsignee = new HashMap<>();
			Map<Integer, Localization> mapLocalization = new HashMap<>();

			while (rs.next()) {

				Sender searchSender = mapSender.get(rs.getInt("id_sender"));
				if (searchSender == null) {
					searchSender = instantiateSender(rs);
					mapSender.put(rs.getInt("id_sender"), searchSender);
				}

				Consignee searchConsignee = mapConsignee.get(rs.getInt("id_consignee"));
				if (searchConsignee == null) {
					searchConsignee = instantiateConsignee(rs);
					mapConsignee.put(rs.getInt("id_consignee"), searchConsignee);
				}

				Localization searchLocalization = mapLocalization.get(rs.getInt("id_localization"));
				if (searchLocalization == null) {
					searchLocalization = instantiateLocalization(rs);
					mapLocalization.put(rs.getInt("id_localization"), searchLocalization);
				}

				PackageDelivery obj = instantiatePackageDelivery(rs, searchSender, searchConsignee, searchLocalization);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<PackageDelivery> findByLocalization(Localization localization) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_package_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_package_delivery "
					+ "INNER JOIN tb_sender ON tb_package_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_package_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_package_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE id_localization = ? " 
					+ "ORDER BY tb_localization.city");

			st.setInt(1, localization.getId());

			rs = st.executeQuery();

			List<PackageDelivery> list = new ArrayList<>();
			Map<Integer, Sender> mapSender = new HashMap<>();
			Map<Integer, Consignee> mapConsignee = new HashMap<>();
			Map<Integer, Localization> mapLocalization = new HashMap<>();

			while (rs.next()) {

				Sender searchSender = mapSender.get(rs.getInt("id_sender"));
				if (searchSender == null) {
					searchSender = instantiateSender(rs);
					mapSender.put(rs.getInt("id_sender"), searchSender);
				}

				Consignee searchConsignee = mapConsignee.get(rs.getInt("id_consignee"));
				if (searchConsignee == null) {
					searchConsignee = instantiateConsignee(rs);
					mapConsignee.put(rs.getInt("id_consignee"), searchConsignee);
				}

				Localization searchLocalization = mapLocalization.get(rs.getInt("id_localization"));
				if (searchLocalization == null) {
					searchLocalization = instantiateLocalization(rs);
					mapLocalization.put(rs.getInt("id_localization"), searchLocalization);
				}

				PackageDelivery obj = instantiatePackageDelivery(rs, searchSender, searchConsignee, searchLocalization);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Sender instantiateSender(ResultSet rs) throws SQLException {
		Sender sender = new Sender();
		sender.setId(rs.getInt("id_sender"));
		sender.setName(rs.getString("sender"));
		return sender;
	}

	private Consignee instantiateConsignee(ResultSet rs) throws SQLException {
		Consignee consignee = new Consignee();
		consignee.setId(rs.getInt("id_consignee"));
		consignee.setName(rs.getString("consignee"));
		consignee.setPersonIdentifier(rs.getString("person_identifier"));
		consignee.setTypeOfPersonIdentifier(TypeOfPersonIdentifier.valueOf(rs.getString("type_of_person")));
		return consignee;
	}

	private Localization instantiateLocalization(ResultSet rs) throws SQLException {
		Localization localization = new Localization();
		localization.setId(rs.getInt("id_localization"));
		localization.setCountry(rs.getString("country"));
		localization.setState(rs.getString("state"));
		localization.setCity(rs.getString("city"));
		return localization;
	}

	private PackageDelivery instantiatePackageDelivery(ResultSet rs, Sender sender, Consignee consignee,
			Localization localization) throws SQLException {
		PackageDelivery obj = new PackageDelivery();
		obj.setId(rs.getInt("id_package_delivery"));
		obj.setWeight(rs.getDouble("weight"));
		obj.setSender(sender);
		obj.setConsignee(consignee);
		obj.setLocalization(localization);
		return obj;
	}
}
