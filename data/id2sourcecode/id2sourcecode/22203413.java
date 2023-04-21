        public synchronized void lock(String read[], String write[]) throws RequestException {
            if (locked) {
                throw new RequestException("failed to lock because tables already locked", Reason.LOCKED);
            }
            String sql = "LOCK TABLES ";
            String separator = ", ";
            if (read != null) for (String table : read) {
                sql += table + " READ" + separator;
            }
            if (write != null) for (String table : write) {
                sql += table + " WRITE" + separator;
            }
            sql = sql.substring(0, sql.length() - separator.length());
            try {
                executeUpdate(sql);
                locked = true;
            } catch (RequestException e) {
                log.error("didn't manage to lock: " + sql);
                throw e;
            }
        }
