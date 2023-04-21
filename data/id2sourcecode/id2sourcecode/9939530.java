    public final void testAddAdjacentSpecificToDef() throws MarshalException, ValidationException, IOException {
        String snmpConfigXml = "<?xml version=\"1.0\"?>\n" + "<snmp-config retry=\"3\" timeout=\"800\"\n" + "   read-community=\"public\" write-community=\"private\">\n" + "   <definition version=\"v2c\">\n" + "       <specific>192.168.0.5</specific>\n" + "   </definition>\n" + "\n" + "</snmp-config>\n" + "";
        Reader rdr = new StringReader(snmpConfigXml);
        SnmpPeerFactory.setInstance(new SnmpPeerFactory(rdr));
        SnmpConfigManager mgr = new SnmpConfigManager(SnmpPeerFactory.getSnmpConfig());
        SnmpEventInfo info = new SnmpEventInfo();
        info.setVersion("v2c");
        info.setFirstIPAddress("192.168.0.6");
        MergeableDefinition configDef = new MergeableDefinition(SnmpPeerFactory.getSnmpConfig().getDefinition(0));
        MergeableDefinition matchingDef = mgr.findDefMatchingAttributes(info.createDef());
        assertNotNull(matchingDef);
        assertFalse(matchingDef.hasMatchingSpecific(info.getFirstIPAddress()));
        assertEquals(1, matchingDef.getConfigDef().getSpecificCount());
        assertEquals(0, matchingDef.getConfigDef().getRangeCount());
        assertNull(configDef.getConfigDef().getReadCommunity());
        mgr.mergeIntoConfig(info.createDef());
        assertEquals(matchingDef.getConfigDef(), mgr.findDefMatchingAttributes(info.createDef()).getConfigDef());
        assertFalse(matchingDef.hasMatchingSpecific(info.getFirstIPAddress()));
        assertEquals(0, matchingDef.getConfigDef().getSpecificCount());
        assertEquals(1, matchingDef.getConfigDef().getRangeCount());
        assertEquals("192.168.0.5", matchingDef.getConfigDef().getRange(0).getBegin());
        assertEquals("192.168.0.6", matchingDef.getConfigDef().getRange(0).getEnd());
        assertNull(configDef.getConfigDef().getReadCommunity());
    }
