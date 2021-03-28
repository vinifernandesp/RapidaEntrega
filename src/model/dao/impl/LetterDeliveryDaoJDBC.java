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
import model.dao.LetterDeliveryDao;
import model.entities.Consignee;
import model.entities.LetterDelivery;
import model.entities.Localization;
import model.entities.Sender;
import model.enumerator.TypeOfPersonIdentifier;

public class LetterDeliveryDaoJDBC implements LetterDeliveryDao {

	private Connection conn;

	public LetterDeliveryDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(LetterDelivery obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO tb_letter_delivery "
					+ "(envelope, id_localization, id_consignee, id_sender) " 
					+ "VALUES " 
					+ "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setBoolean(1, obj.isEnvelope());
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
	public void update(LetterDelivery obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE tb_letter_delivery "
					+ "SET envelope = ?, id_localization = ?, id_consignee = ?, id_sender = ? "
					+ "WHERE id_letter_delivery = ?");

			st.setBoolean(1, obj.isEnvelope());
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
			st = conn.prepareStatement("DELETE FROM tb_letter_delivery WHERE id_letter_delivery = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public LetterDelivery findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_letter_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_letter_delivery "
					+ "INNER JOIN tb_sender ON tb_letter_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_letter_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_letter_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE tb_letter_delivery.id_letter_delivery = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Sender sender = instantiateSender(rs);
				Consignee consignee = instantiateConsignee(rs);
				Localization localization = instantiateLocalization(rs);
				LetterDelivery obj = instantiateLetterDelivery(rs, sender, consignee, localization);
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
	public List<LetterDelivery> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_letter_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_letter_delivery "
					+ "INNER JOIN tb_sender ON tb_letter_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_letter_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_letter_delivery.id_localization = tb_localization.id_localization");

			rs = st.executeQuery();

			List<LetterDelivery> list = new ArrayList<>();
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

				LetterDelivery obj = instantiateLetterDelivery(rs, searchSender, searchConsignee, searchLocalization);
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
	public List<LetterDelivery> findBySender(Sender sender) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_letter_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_letter_delivery "
					+ "INNER JOIN tb_sender ON tb_letter_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_letter_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_letter_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE id_sender = ? " 
					+ "ORDER BY tb_sender.name");

			st.setInt(1, sender.getId());

			rs = st.executeQuery();

			List<LetterDelivery> list = new ArrayList<>();
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

				LetterDelivery obj = instantiateLetterDelivery(rs, searchSender, searchConsignee, searchLocalization);
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
	public List<LetterDelivery> findByConsignee(Consignee consignee) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_letter_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_letter_delivery "
					+ "INNER JOIN tb_sender ON tb_letter_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_letter_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_letter_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE id_consignee = ? " 
					+ "ORDER BY tb_consignee.name");

			st.setInt(1, consignee.getId());

			rs = st.executeQuery();

			List<LetterDelivery> list = new ArrayList<>();
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

				LetterDelivery obj = instantiateLetterDelivery(rs, searchSender, searchConsignee, searchLocalization);
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
	public List<LetterDelivery> findByLocalization(Localization localization) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_letter_delivery.*, tb_sender.name as sender, "
					+ "tb_consignee.name as consignee, tb_consignee.person_identifier as person_identifier, tb_consignee.type_of_person_identifier as type_of_person, "
					+ "tb_localization.country as country, tb_localization.state as state, tb_localization.city as city "
					+ "FROM tb_letter_delivery "
					+ "INNER JOIN tb_sender ON tb_letter_delivery.id_sender = tb_sender.id_sender "
					+ "INNER JOIN tb_consignee ON tb_letter_delivery.id_consignee = tb_consignee.id_consignee "
					+ "INNER JOIN tb_localization ON tb_letter_delivery.id_localization = tb_localization.id_localization "
					+ "WHERE id_localization = ? " 
					+ "ORDER BY tb_localization.city");

			st.setInt(1, localization.getId());

			rs = st.executeQuery();

			List<LetterDelivery> list = new ArrayList<>();
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

				LetterDelivery obj = instantiateLetterDelivery(rs, searchSender, searchConsignee, searchLocalization);
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

	private LetterDelivery instantiateLetterDelivery(ResultSet rs, Sender sender, Consignee consignee,
			Localization localization) throws SQLException {
		LetterDelivery obj = new LetterDelivery();
		obj.setId(rs.getInt("id_letter_delivery"));
		obj.setEnvelope(rs.getBoolean("envelope"));
		obj.setSender(sender);
		obj.setConsignee(consignee);
		obj.setLocalization(localization);
		return obj;
	}
}
