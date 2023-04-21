    public void testNewSpecifcSameAsBeginInOldDef() throws MarshalException, ValidationException, IOException {
        String snmpConfigXml = "<?xml version=\"1.0\"?>\n" + "<snmp-config retry=\"3\" timeout=\"800\"\n" + "   read-community=\"public\" write-community=\"private\">\n" + "   <definition version=\"v2c\">\n" + "       <range begin=\"192.168.0.3\" end=\"192.168.0.100\"/>" + "   </definition>\n" + "\n" + "</snmp-config>\n" + "";
        Reader rdr = new StringReader(snmpConfigXml);
        SnmpPeerFactory.setInstance(new SnmpPeerFactory(rdr));
        SnmpConfigManager mgr = new SnmpConfigManager(SnmpPeerFactory.getSnmpConfig());
        SnmpEventInfo info = new SnmpEventInfo();
        info.setVersion("v1");
        info.setFirstIPAddress("192.168.0.3");
        mgr.mergeIntoConfig(info.createDef());
        assertEquals(2, mgr.getConfig().getDefinitionCount());
        assertEquals(1, mgr.getConfig().getDefinition(0).getRangeCount());
        assertEquals(0, mgr.getConfig().getDefinition(0).getSpecificCount());
        assertEquals(0, mgr.getConfig().getDefinition(1).getRangeCount());
        assertEquals(1, mgr.getConfig().getDefinition(1).getSpecificCount());
        assertEquals("192.168.0.4", mgr.getConfig().getDefinition(0).getRange(0).getBegin());
        assertEquals("192.168.0.100", mgr.getConfig().getDefinition(0).getRange(0).getEnd());
        assertEquals("192.168.0.3", mgr.getConfig().getDefinition(1).getSpecific(0));
    }
