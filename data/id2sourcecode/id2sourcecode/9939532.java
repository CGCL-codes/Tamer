    public void testRemoveSpecificFromRange() throws MarshalException, ValidationException, IOException {
        String snmpConfigXml = "<?xml version=\"1.0\"?>\n" + "<snmp-config retry=\"3\" timeout=\"800\"\n" + "   read-community=\"public\" write-community=\"private\">\n" + "   <definition version=\"v2c\">\n" + "       <range begin=\"192.168.1.100\" end=\"192.168.1.200\"/>" + "   </definition>\n" + "\n" + "</snmp-config>\n" + "";
        Reader rdr = new StringReader(snmpConfigXml);
        SnmpPeerFactory.setInstance(new SnmpPeerFactory(rdr));
        SnmpConfigManager mgr = new SnmpConfigManager(SnmpPeerFactory.getSnmpConfig());
        SnmpEventInfo info = new SnmpEventInfo();
        info.setVersion("v1");
        info.setFirstIPAddress("192.168.1.120");
        mgr.mergeIntoConfig(info.createDef());
        assertEquals(2, mgr.getConfig().getDefinitionCount());
        assertEquals(2, mgr.getConfig().getDefinition(0).getRangeCount());
        assertEquals(0, mgr.getConfig().getDefinition(0).getSpecificCount());
        assertEquals("192.168.1.100", mgr.getConfig().getDefinition(0).getRange(0).getBegin());
        assertEquals("192.168.1.119", mgr.getConfig().getDefinition(0).getRange(0).getEnd());
        assertEquals("192.168.1.121", mgr.getConfig().getDefinition(0).getRange(1).getBegin());
        assertEquals("192.168.1.200", mgr.getConfig().getDefinition(0).getRange(1).getEnd());
        assertEquals(0, mgr.getConfig().getDefinition(1).getRangeCount());
        assertEquals(1, mgr.getConfig().getDefinition(1).getSpecificCount());
        assertEquals("192.168.1.120", mgr.getConfig().getDefinition(1).getSpecific(0));
    }
