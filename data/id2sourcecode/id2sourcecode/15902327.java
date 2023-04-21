    public static Vector upgrade030(Vector messages) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String sql = "drop table jam_image";
            DatabaseConnection.executeUpdate(sql, conn);
            messages.add("Dropped jam_image table");
            DatabaseConnection.executeUpdate(DefaultQueryHandler.STATEMENT_CREATE_CATEGORY_TABLE, conn);
            messages.add("Added jam_category table");
            conn.commit();
        } catch (Exception e) {
            try {
                DatabaseConnection.executeUpdate(DefaultQueryHandler.STATEMENT_DROP_CATEGORY_TABLE, conn);
            } catch (Exception ex) {
            }
            conn.rollback();
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return messages;
    }
