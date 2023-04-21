package fido.db;

import java.sql.*;
import java.util.*;
import fido.util.FidoDataSource;

/**
 * 
 */
public class QuestionWordTable {

    public static final int WHO = 1;

    public static final int WHAT = 2;

    public static final int WHERE = 3;

    public static final int WHEN = 4;

    public static final int HOW = 5;

    public static final int WHY = 6;

    /**
	 * 
	 */
    public QuestionWordTable() {
    }

    /**
	 * 
	 */
    public void add(String word, int type) throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                String sql;
                conn = FidoDataSource.getConnection();
                stmt = conn.createStatement();
                if (contains(stmt, word) == true) {
                    sql = "update QuestionWords set Type = '" + type + "'" + "where QuestionWord = '" + word + "'";
                } else {
                    sql = "insert into QuestionWords (QuestionWord, Type) " + "values ('" + word + "', " + type + ")";
                }
                stmt.executeUpdate(sql);
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }

    private boolean contains(Statement stmt, String word) throws SQLException {
        ResultSet rs = null;
        try {
            String sql = "select count(1) from QuestionWords where QuestionWord = '" + word + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next() == false) throw new SQLException("No rows returned for count(1) query"); else {
                int num = rs.getInt(1);
                if (num == 1) return true;
                return false;
            }
        } finally {
            if (rs != null) rs.close();
        }
    }

    /**
	 * 
	 */
    public void delete(String word) throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                String sql = "delete from QuestionWords where QuestionWord = '" + word + "'";
                conn = fido.util.FidoDataSource.getConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }

    /**
	 * Returns the GrammarLink type referrenced by the parameter <i>name</i>.
	 * @param type The type of link referrenced by <i>name</i>
	 * @return type of link, null if none found
	 */
    public QuestionWord get(String word) throws FidoDatabaseException, QuestionWordNotFoundException {
        try {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select Type from QuestionWords " + "where QuestionWord = '" + word + "'";
                conn = fido.util.FidoDataSource.getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next() == false) throw new QuestionWordNotFoundException(word); else {
                    QuestionWord item = new QuestionWord(word, rs.getInt(1));
                    return item;
                }
            } finally {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }

    /**
	 * Returns the names of all GrammarLink names in the list.  The names
	 * of the links are in alphabetical order.
	 * @return list of link names
	 */
    public Collection list() throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select a.QuestionWord, a.Type, b.Description " + "from QuestionWords a, QuestionWordTypes b " + "where a.Type = b.Type " + "order by QuestionWord";
                conn = fido.util.FidoDataSource.getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                Vector list = new Vector();
                while (rs.next() == true) {
                    QuestionWord item = new QuestionWord(rs.getString(1), rs.getInt(2), rs.getString(3));
                    list.add(item);
                }
                return list;
            } finally {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }

    public Collection listTypes() throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select Type, Description from QuestionWordTypes order by Type";
                conn = fido.util.FidoDataSource.getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                Vector list = new Vector();
                while (rs.next() == true) {
                    QuestionWordType type = new QuestionWordType(rs.getInt(1), rs.getString(2));
                    list.add(type);
                }
                return list;
            } finally {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }

    public int hashCode(String word) throws FidoDatabaseException, QuestionWordNotFoundException {
        QuestionWord qw = get(word);
        Vector list = new Vector();
        list.add(word);
        list.add(new Integer(qw.getType()));
        return list.hashCode();
    }
}
