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
import model.dao.LocalizationDao;
import model.entities.Localization;

public class LocalizationDaoJDBC implements LocalizationDao {

	private Connection conn;

	public LocalizationDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Localization obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO tb_localization " 
					+ "(country, state, city) " 
					+ "VALUES " 
					+ "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getCountry());
			st.setString(2, obj.getState());
			st.setString(3, obj.getCity());

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
	public void update(Localization obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE tb_localization " 
					+ "SET country = ?, state = ?, city = ? " 
					+ "WHERE id_localization = ?");

			st.setString(1, obj.getCountry());
			st.setString(2, obj.getState());
			st.setString(3, obj.getCity());
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
			st = conn.prepareStatement("DELETE FROM tb_localization WHERE id_localization = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Localization findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM tb_localization WHERE id_localization = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Localization obj = new Localization();
				obj.setId(rs.getInt("id_localization"));
				obj.setCountry(rs.getString("country"));
				obj.setState(rs.getString("state"));
				obj.setCity(rs.getString("city"));
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
	public List<Localization> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM tb_localization");
			rs = st.executeQuery();

			List<Localization> list = new ArrayList<>();

			while (rs.next()) {
				Localization obj = new Localization();
				obj.setId(rs.getInt("id_localization"));
				obj.setCountry(rs.getString("country"));
				obj.setState(rs.getString("state"));
				obj.setCity(rs.getString("city"));
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