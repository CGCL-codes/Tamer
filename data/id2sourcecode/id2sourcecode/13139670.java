    static void test() throws SQLException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        Savepoint sp = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            st = conn.createStatement();
            String sql = "update user set money=money-10 where id=15";
            st.executeUpdate(sql);
            sp = conn.setSavepoint();
            sql = "update user set money=money-10 where id=12";
            st.executeUpdate(sql);
            sql = "select money from user where id=13";
            rs = st.executeQuery(sql);
            float money = 0.0f;
            while (rs.next()) {
                money = rs.getFloat("money");
            }
            if (money > 1000) throw new RuntimeException("�Ѿ��������ֵ��");
            sql = "update user set money=money+10 where id=13";
            st.executeUpdate(sql);
            conn.commit();
        } catch (RuntimeException e) {
            if (conn != null && sp != null) {
                conn.rollback(sp);
                conn.commit();
            }
            throw e;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            JdbcUtils.free(rs, st, conn);
        }
    }
