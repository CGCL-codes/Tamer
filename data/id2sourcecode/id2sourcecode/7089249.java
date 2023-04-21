        <T extends Throwable, T2> T2 getDBItem(Statement stmt, String sql, Class<T2> type, Thrower<T> thrower) throws T {
            boolean closeStmt = stmt == null;
            java.sql.ResultSet rs = null;
            try {
                if (stmt == null) stmt = getConnection(thrower).createStatement();
                rs = stmt.executeQuery(sql);
                if (!rs.next()) thrower.error("No rows selected: SQL=" + sql);
                Object ret;
                if (type != null) {
                    if (String.class.equals(type)) ret = rs.getString(1); else if (Number.class.isAssignableFrom(type)) ret = Number.class.cast(rs.getObject(1)); else if (java.util.Date.class.equals(type)) ret = new java.util.Date(rs.getTimestamp(1).getTime()); else if (byte.class.equals(type)) {
                        java.io.InputStream stream = rs.getBinaryStream(1);
                        java.io.ByteArrayOutputStream retStr = new java.io.ByteArrayOutputStream();
                        try {
                            int read = stream.read();
                            while (read >= 0) {
                                retStr.write(read);
                                read = stream.read();
                            }
                        } catch (java.io.IOException e) {
                            thrower.error("Could not read binary data: SQL=" + sql, e);
                            throw new IllegalStateException("Thrower didn't throw an exception!");
                        } finally {
                            try {
                                stream.close();
                            } catch (java.io.IOException e) {
                                log.error("Could not close database input stream", e);
                            }
                        }
                        return (T2) retStr.toByteArray();
                    } else {
                        thrower.error("Unrecognized databased item type: " + type.getName());
                        throw new IllegalStateException("Thrower didn't throw an exception!");
                    }
                    ret = type.cast(ret);
                } else {
                    int count = rs.getMetaData().getColumnCount();
                    if (count == 1) return (T2) rs.getObject(1);
                    Object[] retA = new Object[count];
                    for (int i = 0; i < count; i++) retA[i] = rs.getObject(i + 1);
                    ret = retA;
                }
                if (rs.next()) {
                    thrower.error("Multiple rows selected: SQL=" + sql);
                    throw new IllegalStateException("Thrower didn't throw an exception!");
                }
                rs.close();
                rs = null;
                return (T2) ret;
            } catch (SQLException e) {
                thrower.error("Could not get databased field: SQL=" + sql, e);
                throw new IllegalStateException("Thrower didn't throw an exception!");
            } catch (ClassCastException e) {
                thrower.error("Databased field is not of type " + type.getName() + ": SQL=" + sql, e);
                throw new IllegalStateException("Thrower didn't throw an exception!");
            } finally {
                if (rs != null) try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
                if (stmt != null && closeStmt) try {
                    stmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        }
