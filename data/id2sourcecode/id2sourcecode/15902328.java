    public static Vector upgrade031(Vector messages) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String sql = "alter table jam_topic add column redirect_to VARCHAR(200) ";
            DatabaseConnection.executeUpdate(sql, conn);
            messages.add("Added redirect_to column to table jam_topic");
            sql = "alter table jam_topic add column delete_date TIMESTAMP ";
            DatabaseConnection.executeUpdate(sql, conn);
            messages.add("Added delete_date column to table jam_topic");
            sql = "alter table jam_topic drop constraint jam_unique_topic_name_vwiki ";
            DatabaseConnection.executeUpdate(sql, conn);
            sql = "alter table jam_topic add constraint jam_unique_topic_name_vwiki UNIQUE (topic_name, virtual_wiki_id, delete_date) ";
            DatabaseConnection.executeUpdate(sql, conn);
            messages.add("Updated unique topic name constraint");
            sql = "update jam_topic set delete_date = ? where topic_deleted = '1' ";
            WikiPreparedStatement stmt = new WikiPreparedStatement(sql);
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate(conn);
            messages.add("Updated deleted topics in jam_topic");
            sql = "alter table jam_topic drop column topic_deleted ";
            DatabaseConnection.executeUpdate(sql, conn);
            messages.add("Dropped column topic_deleted from table jam_topic");
            sql = "alter table jam_file add column delete_date TIMESTAMP ";
            DatabaseConnection.executeUpdate(sql, conn);
            messages.add("Added delete_date column to table jam_file");
            sql = "update jam_file set delete_date = ? where file_deleted = '1' ";
            stmt = new WikiPreparedStatement(sql);
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate(conn);
            messages.add("Updated deleted files in jam_file");
            sql = "alter table jam_file drop column file_deleted ";
            DatabaseConnection.executeUpdate(sql, conn);
            messages.add("Dropped column file_deleted from table jam_file");
            if (!Environment.getValue(Environment.PROP_DB_TYPE).equals("mysql")) {
                sql = "alter table jam_wiki_user drop constraint jam_unique_wiki_user_login ";
            } else {
                sql = "alter table jam_wiki_user drop index jam_unique_wiki_user_login ";
            }
            DatabaseConnection.executeUpdate(sql, conn);
            DatabaseConnection.executeUpdate(DefaultQueryHandler.STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX, conn);
            messages.add("Updated unique wiki user login constraint");
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return messages;
    }
