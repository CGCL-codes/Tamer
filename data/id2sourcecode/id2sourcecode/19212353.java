    public void setUp() throws Exception {
        File dir = new File("target/test-work-dir");
        dir.mkdirs();
        m_configFile = File.createTempFile("snmp-config-" + getName() + "-", "xml", dir);
        FileUtils.writeStringToFile(m_configFile, "<?xml version=\"1.0\"?>" + "<snmp-config port=\"9161\" retry=\"1\" timeout=\"2000\"\n" + "             read-community=\"myPublic\" \n" + "             version=\"v1\" \n" + "             max-vars-per-pdu=\"27\"  />");
        SnmpPeerFactory.setFile(m_configFile);
        FactoryBasedSnmpConfigDao factoryBasedSnmpConfigDao = new FactoryBasedSnmpConfigDao();
        factoryBasedSnmpConfigDao.afterPropertiesSet();
        m_snmpConfigDao = factoryBasedSnmpConfigDao;
    }
