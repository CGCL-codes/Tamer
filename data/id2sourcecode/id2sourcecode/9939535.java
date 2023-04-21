    public void testInvalidRange() throws MarshalException, ValidationException, IOException {
        String snmpConfigXml = "<?xml version=\"1.0\"?>\n" + "<snmp-config retry=\"3\" timeout=\"800\"\n" + "   read-community=\"public\" write-community=\"private\">\n" + "   <definition version=\"v2c\">\n" + "       <range begin=\"192.168.0.3\" end=\"192.168.0.100\"/>" + "   </definition>\n" + "\n" + "</snmp-config>\n" + "";
        Reader rdr = new StringReader(snmpConfigXml);
        SnmpPeerFactory.setInstance(new SnmpPeerFactory(rdr));
        SnmpConfigManager mgr = new SnmpConfigManager(SnmpPeerFactory.getSnmpConfig());
        SnmpEventInfo info = new SnmpEventInfo();
        info.setVersion("v1");
        info.setFirstIPAddress("192.168.0.3");
        info.setLastIPAddress("192.168.0.1");
        try {
            mgr.mergeIntoConfig(info.createDef());
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
        }
    }
