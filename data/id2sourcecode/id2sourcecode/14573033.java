    public static synchronized void setup(String clientVersion) throws SQLException {
        synchronized (commonSession) {
            String dbVersion;
            try {
                dbVersion = (String) commonSession.createSQLQuery("select version from spamwatch_metadata").addScalar("version", Hibernate.STRING).uniqueResult();
            } catch (Exception e) {
                dbVersion = "not found";
            }
            if (!dbVersion.equals(clientVersion)) {
                String message = "Client version (" + clientVersion + ") differs from database version (" + dbVersion + ")!\nThe database will be updated now.";
                int result = JOptionPane.showConfirmDialog(null, message, "Database not compatible", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.CANCEL_OPTION) {
                    System.out.println("Database update canceled.");
                    System.exit(1);
                }
                if (dbVersion.equals("not found")) {
                    Transaction tx = commonSession.beginTransaction();
                    try {
                        InputStream in = ClassLoader.getSystemResourceAsStream("spamwatch/base/sql/2.2.sql");
                        BufferedReader r = new BufferedReader(new InputStreamReader(in));
                        while (r.ready()) {
                            commonSession.createSQLQuery(r.readLine()).executeUpdate();
                        }
                        tx.commit();
                        dbVersion = "2.2";
                    } catch (Exception exc) {
                        tx.rollback();
                        throw new SQLException("Exception while updating.", exc);
                    }
                }
                backup();
            }
        }
    }
