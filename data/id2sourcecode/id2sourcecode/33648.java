    protected XDocument load(String baseURI) throws SQLException, XModelException {
        Connection conn = null;
        try {
            println("Trying to get a JDBC connection...");
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.close();
            } catch (SQLException se) {
            }
            throw new XModelException("Failed to create a JDBC connection");
        }
        ModelLoader loader = null;
        XDocument xdoc = null;
        try {
            loader = new ModelLoader(conn);
            println("Trying to load the model from the database tables...");
            xdoc = loader.loadDocument(baseURI);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.close();
            } catch (SQLException se) {
            }
            throw new XModelException("Failed loading from the database");
        }
        loader.commit();
        try {
            conn.close();
        } catch (SQLException e) {
        }
        return xdoc;
    }
