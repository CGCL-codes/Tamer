    public static int deleteAll(Class<? extends Record> c, String attributes, Integer... ids) throws RecordException {
        Logger log = new Logger(RecordAbstract.class);
        Connection conn = ConnectionManager.getConnection();
        LoggableStatement pStat = null;
        StatementBuilder builder = null;
        if (!attributes.startsWith("{")) {
            attributes = "{" + attributes;
        }
        if (!attributes.endsWith("}")) {
            attributes += "}";
        }
        try {
            JSONObject parsed = new JSONObject(attributes);
            if (ids.length == 0) {
                builder = new StatementBuilder("delete from " + TableNameResolver.getTableName(c));
            } else {
                builder = new StatementBuilder("delete from " + TableNameResolver.getTableName(c) + " where id in (");
                for (int i = 0; i < ids.length; ++i) {
                    builder.append(":id" + i);
                    if (i != ids.length - 1) {
                        builder.append(", ");
                    }
                    builder.set(":id" + i, ids[i]);
                }
                builder.append(")");
            }
            addConditions(builder, parsed, ids.length == 0);
            pStat = builder.getPreparedStatement(conn);
            log.log(pStat.getQueryString());
            int i = pStat.executeUpdate();
            return i;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback");
            }
            throw new RecordException(e);
        } finally {
            try {
                if (pStat != null) {
                    pStat.close();
                }
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                throw new RecordException("Error closing connection");
            }
        }
    }
