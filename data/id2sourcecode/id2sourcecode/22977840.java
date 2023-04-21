    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Connection conn = null;
        URL url = null;
        try {
            conn = ((DataSource) new InitialContext().lookup("java:comp/env/jdbc/JavalidTestDS")).getConnection();
            if (System.getProperty("install.oracle") == null) {
                url = this.getClass().getResource("org/javalid/test/web/db/config/hsqldb-install.sql");
                if (url == null) {
                    url = this.getClass().getResource("/org/javalid/test/web/db/config/hsqldb-install.sql");
                }
            } else {
                url = this.getClass().getResource("org/javalid/test/web/db/config/oracle-install.sql");
                if (url == null) {
                    url = this.getClass().getResource("/org/javalid/test/web/db/config/oracle-install.sql");
                }
            }
            BufferedInputStream in = new BufferedInputStream(url.openConnection().getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            out.close();
            in.close();
            PreparedStatement st = conn.prepareStatement(out.toString());
            st.execute();
            st.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException("FAILED", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        validator = new AnnotationValidatorImpl("org/javalid/test/web/db/config/javalid-config.xml");
    }
