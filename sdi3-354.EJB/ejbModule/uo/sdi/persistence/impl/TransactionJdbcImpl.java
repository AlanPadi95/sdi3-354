package uo.sdi.persistence.impl;

import java.sql.Connection;
import java.sql.SQLException;

import uo.sdi.persistence.PersistenceException;
import uo.sdi.persistence.Transaction;
import uo.sdi.persistence.util.JdbcHelper;

public class TransactionJdbcImpl implements Transaction {

	private Connection con;
	private static String CONFIG_FILE = "/persistence.properties";
	private static JdbcHelper jdbcHelper = new JdbcHelper(CONFIG_FILE);

	@Override
	public void begin() {
		assertNullConnection();
		con = jdbcHelper.createConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void commit() {
		assertNonNullConnection();
		assertOpenConnection();
		try {
			con.commit();
			con.setAutoCommit(true); // makes Jdbc.close() to really close
										// connection
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			jdbcHelper.close(null, con);
		}
	}

	@Override
	public void rollback() {
		assertNonNullConnection();
		assertOpenConnection();
		try {
			con.rollback();
			con.setAutoCommit(true); // makes Jdbc.close() to really close
										// connection
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			jdbcHelper.close(null, con);
		}
	}

	private void assertNullConnection() {
		if (con == null)
			return;
		throw new PersistenceException("Transaction is already initiated");
	}

	private void assertNonNullConnection() {
		if (con != null)
			return;
		throw new PersistenceException("Transaction is not initiated. "
				+ "Call begin() method before use it.");
	}

	private void assertOpenConnection() {
		if (connectionIsOpen())
			return;
		throw new PersistenceException("Transaction is not initiated. "
				+ "Call begin() method before use it.");
	}

	private boolean connectionIsOpen() {
		try {
			return !con.isClosed();
		} catch (SQLException e) {
			throw new PersistenceException("Unexpected JDBC error");
		}
	}

}
