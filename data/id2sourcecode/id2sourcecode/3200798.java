    public ScrumModelAccess() {
        try {
            Properties prop = new Properties();
            URL url = ModelAccess.class.getClassLoader().getResource("u3dserver.properties");
            prop.load(url.openStream());
            String database = prop.getProperty("databaseIA", "//localhost/2bsoft");
            String user = prop.getProperty("userIA", "root");
            String password = prop.getProperty("passwordIA", "");
            System.out.println("Intentando cargar el conector...");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Conectando a la base de IA...");
            con = DriverManager.getConnection("jdbc:mysql:" + database, user, password);
            stmt = con.createStatement();
            System.out.println("Conexion a BD establecida con la base de IA");
        } catch (SQLException ex) {
            System.out.println("Error de mysql: " + ex.getLocalizedMessage() + " " + ex.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Se produjo un error inesperado: " + e.getMessage());
        }
    }
