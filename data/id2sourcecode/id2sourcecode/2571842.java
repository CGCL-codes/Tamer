    @Override
    public void beforeServletStart() throws Exception {
        File dir = new File("target/test-work-dir");
        dir.mkdirs();
        m_snmpConfigFile = File.createTempFile("snmp-config-", "xml");
        FileUtils.writeStringToFile(m_snmpConfigFile, "<?xml version=\"1.0\"?>" + "<snmp-config port=\"9161\" retry=\"1\" timeout=\"2000\"\n" + "             read-community=\"myPublic\" \n" + "             version=\"v1\" \n" + "             max-vars-per-pdu=\"100\"  />");
        SnmpPeerFactory.setFile(m_snmpConfigFile);
        m_jaxbContext = JAXBContext.newInstance(SnmpInfo.class);
    }
