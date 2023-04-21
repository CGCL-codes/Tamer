    public static synchronized XFormTransactionFactory newInstance() {
        Properties prop = new Properties();
        @SuppressWarnings("static-access") URL url = Thread.currentThread().getContextClassLoader().getResource("mwt/xml/xdbforms/configuration/xdbforms.properties");
        try {
            InputStream inStream = url.openStream();
            prop.load(inStream);
            String jdbcConnection = prop.getProperty("jdbc.connection");
            if (jdbcConnection.equals("drivermanager")) {
                return new XFormTransactionFactoryDM();
            } else {
                return new XFormTransactionFactoryDS();
            }
        } catch (IOException ex) {
            Logger.getLogger(XFormTransactionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
