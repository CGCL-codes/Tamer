    public static Vector upgrade010(Vector messages) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String sql = "alter table jam_virtual_wiki add column default_topic_name VARCHAR(200)";
            DatabaseConnection.executeUpdate(sql, conn);
            sql = "update jam_virtual_wiki set default_topic_name = ?";
            WikiPreparedStatement stmt = new WikiPreparedStatement(sql);
            stmt.setString(1, Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC));
            stmt.executeUpdate(conn);
            sql = "alter table jam_virtual_wiki alter column default_topic_name set NOT NULL";
            DatabaseConnection.executeUpdate(sql, conn);
            conn.commit();
            messages.add("Updated jam_virtual_wiki table");
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return messages;
    }
