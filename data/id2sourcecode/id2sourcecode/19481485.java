    public C2IBTrader(String c2ConfigPath) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, C2ATIError {
        InputStream is = getClass().getClassLoader().getResourceAsStream(c2ConfigPath);
        Properties prop = new Properties();
        if (is != null) {
            prop.load(is);
        }
        String eMail = prop.getProperty("eMail", "email@company.com");
        String password = prop.getProperty("password", "password");
        String host = prop.getProperty("host", "host");
        String isLifeAccountStr = prop.getProperty("liveType", "0");
        boolean isLifeAccount = isLifeAccountStr.equals("1");
        if (isLifeAccount) {
            ib = new IBImpl();
            ib.connect("", IB.DEFAULT_PORT, 1);
        } else {
            ib = new IBMockImpl();
        }
        String isLifeSignalsStr = prop.getProperty("liveSignals", "0");
        boolean isLifeSignals = isLifeSignalsStr.equals("1");
        if (isLifeSignals) {
            c2ati = new C2ATI(eMail, password, isLifeAccount, host);
        } else {
            c2ati = new C2ATIMockImpl();
        }
        c2ati.login();
    }
