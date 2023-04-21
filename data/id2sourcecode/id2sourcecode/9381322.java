    public void doHousekeeping() {
        logger.debug("doHousekeeping() called");
        try {
            URL url = new URL(getInitParams().getProperty("test-url", "http://www.iqser.com"));
            url.openConnection().connect();
        } catch (IOException ioe) {
            logger.error("Couldn't open connection for test url - " + ioe.getMessage());
            return;
        }
        Statement stmt1 = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt1 = conn.createStatement();
            rs = stmt1.executeQuery("SELECT * FROM documents WHERE checked<" + lastCrawlerStart + " AND provider='" + getId() + "'");
            while (rs.next()) {
                String s = rs.getString("url");
                try {
                    URL url = new URL(s);
                    url.openConnection().connect();
                    logger.warn("Not checked by the crawler but still available web content");
                } catch (IOException ioe) {
                    logger.error("Couldn't open connection for " + s + " - " + ioe.getMessage());
                    stmt2 = conn.createStatement();
                    stmt2.executeUpdate("DELETE FROM documents WHERE url='" + s + "'" + " AND provider='" + getId() + "'");
                    try {
                        this.removeContent(rs.getString("url"));
                    } catch (IQserException iqe) {
                        logger.error("Couldn't delete object " + s + " - " + iqe.getMessage());
                    }
                }
            }
        } catch (SQLException sqle) {
            logger.error("Couldn't perform sql query or update - " + sqle.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt1 != null) {
                try {
                    stmt1.close();
                } catch (SQLException sqlEx) {
                }
                stmt1 = null;
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException sqlEx) {
                }
                stmt2 = null;
            }
        }
        logger.debug("Housekeeping has finished");
    }
