    public void testRecombineRanges() throws MarshalException, ValidationException, IOException {
        String snmpConfigXml = "<?xml version=\"1.0\"?>\n" + "<snmp-config retry=\"3\" timeout=\"800\"\n" + "   read-community=\"public\" write-community=\"private\">\n" + "   <definition version=\"v2c\">\n" + "       <range begin=\"192.168.1.10\" end=\"192.168.1.14\"/>" + "       <range begin=\"192.168.1.36\" end=\"192.168.1.40\"/>" + "   </definition>\n" + "   <definition version=\"v1\">\n" + "       <range begin=\"192.168.1.15\" end=\"192.168.1.35\"/>" + "   </definition>\n" + "\n" + "</snmp-config>\n" + "";
        Reader rdr = new StringReader(snmpConfigXml);
        SnmpPeerFactory.setInstance(new SnmpPeerFactory(rdr));
        SnmpConfigManager mgr = new SnmpConfigManager(SnmpPeerFactory.getSnmpConfig());
        SnmpEventInfo info = new SnmpEventInfo();
        info.setVersion("v2c");
        info.setFirstIPAddress("192.168.1.15");
        info.setLastIPAddress("192.168.1.35");
        mgr.mergeIntoConfig(info.createDef());
        assertEquals(1, mgr.getConfig().getDefinitionCount());
        assertEquals("192.168.1.10", mgr.getConfig().getDefinition(0).getRange(0).getBegin());
        assertEquals("192.168.1.40", mgr.getConfig().getDefinition(0).getRange(0).getEnd());
    }
