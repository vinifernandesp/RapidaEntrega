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
import model.dao.SenderDao;
import model.entities.Sender;

public class SenderDaoJDBC implements SenderDao {

	private Connection conn;

	public SenderDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Sender obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO tb_sender " + "(name) " + "VALUES " + "(?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

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
	public void update(Sender obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE tb_sender " + "SET name = ? " + "WHERE id_sender = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM tb_sender WHERE id_sender = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Sender findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM tb_sender WHERE id_sender = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Sender obj = new Sender();
				obj.setId(rs.getInt("id_sender"));
				obj.setName(rs.getString("name"));
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
	public List<Sender> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM tb_sender");
			rs = st.executeQuery();

			List<Sender> list = new ArrayList<>();

			while (rs.next()) {
				Sender obj = new Sender();
				obj.setId(rs.getInt("id_sender"));
				obj.setName(rs.getString("name"));
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
