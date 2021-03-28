package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ConsigneeDao;
import model.entities.Consignee;
import model.enumerator.TypeOfPersonIdentifier;

public class ConsigneeDaoJDBC implements ConsigneeDao {

	private Connection conn;

	public ConsigneeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Consignee obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO tb_consignee "
					+ "(name, person_identifier, type_of_person_identifier) " 
					+ "VALUES " 
					+ "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getPersonIdentifier());
			st.setString(3, String.valueOf(obj.getTypeOfPersonIdentifier()));

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
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
	public void update(Consignee obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE tb_consignee "
					+ "SET name = ?, person_identifier = ?, type_of_person_identifier = ? " 
					+ "WHERE id_consignee = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getPersonIdentifier());
			st.setString(3, String.valueOf(obj.getTypeOfPersonIdentifier()));
			st.setInt(4, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM tb_consignee WHERE id_consignee = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Consignee findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM tb_consignee WHERE id_consignee = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Consignee obj = new Consignee();
				obj.setId(rs.getInt("id_consignee"));
				obj.setName(rs.getString("name"));
				obj.setPersonIdentifier(rs.getString("person_identifier"));
				obj.setTypeOfPersonIdentifier(
						TypeOfPersonIdentifier.valueOf(rs.getString("type_of_person_identifier")));
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
	public List<Consignee> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM tb_consignee");
			rs = st.executeQuery();

			List<Consignee> list = new ArrayList<>();

			while (rs.next()) {
				Consignee obj = new Consignee();
				obj.setId(rs.getInt("id_consignee"));
				obj.setName(rs.getString("name"));
				obj.setPersonIdentifier(rs.getString("person_identifier"));
				obj.setTypeOfPersonIdentifier(
						TypeOfPersonIdentifier.valueOf(rs.getString("type_of_person_identifier")));
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
}