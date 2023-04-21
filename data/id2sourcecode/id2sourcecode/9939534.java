    public void testRemoveSpecificInSeparateDefWithNewRange() throws MarshalException, ValidationException, IOException {
        String snmpConfigXml = "<?xml version=\"1.0\"?>\n" + "<snmp-config retry=\"3\" timeout=\"800\"\n" + "   read-community=\"public\" write-community=\"private\">\n" + "   <definition version=\"v2c\">\n" + "       <specific>192.168.1.30</specific>" + "       <specific>10.1.1.1</specific>" + "   </definition>\n" + "\n" + "</snmp-config>\n" + "";
        Reader rdr = new StringReader(snmpConfigXml);
        SnmpPeerFactory.setInstance(new SnmpPeerFactory(rdr));
        SnmpConfigManager mgr = new SnmpConfigManager(SnmpPeerFactory.getSnmpConfig());
        SnmpEventInfo info = new SnmpEventInfo();
        info.setVersion("v1");
        info.setFirstIPAddress("192.168.1.15");
        info.setLastIPAddress("192.168.1.35");
        mgr.mergeIntoConfig(info.createDef());
        assertEquals(2, mgr.getConfig().getDefinitionCount());
        assertEquals(0, mgr.getConfig().getDefinition(0).getRangeCount());
        assertEquals(1, mgr.getConfig().getDefinition(0).getSpecificCount());
        assertEquals(1, mgr.getConfig().getDefinition(1).getRangeCount());
        assertEquals(0, mgr.getConfig().getDefinition(1).getSpecificCount());
        assertEquals("10.1.1.1", mgr.getConfig().getDefinition(0).getSpecific(0));
        assertEquals("192.168.1.15", mgr.getConfig().getDefinition(1).getRange(0).getBegin());
        assertEquals("192.168.1.35", mgr.getConfig().getDefinition(1).getRange(0).getEnd());
    }
