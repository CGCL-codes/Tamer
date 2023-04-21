    public final void testModifySpecificInDef() throws MarshalException, ValidationException, IOException {
        String snmpConfigXml = "<?xml version=\"1.0\"?>\n" + "<snmp-config retry=\"3\" timeout=\"800\"\n" + "   read-community=\"public\" write-community=\"private\">\n" + "   <definition version=\"v2c\">\n" + "       <specific>192.168.0.5</specific>\n" + "   </definition>\n" + "\n" + "</snmp-config>\n" + "";
        Reader rdr = new StringReader(snmpConfigXml);
        SnmpPeerFactory.setInstance(new SnmpPeerFactory(rdr));
        SnmpEventInfo info = new SnmpEventInfo();
        info.setCommunityString("abc");
        info.setFirstIPAddress("192.168.0.5");
        MergeableDefinition configDef = new MergeableDefinition(SnmpPeerFactory.getSnmpConfig().getDefinition(0));
        SnmpConfigManager mgr = new SnmpConfigManager(SnmpPeerFactory.getSnmpConfig());
        MergeableDefinition matchingDef = mgr.findDefMatchingAttributes(info.createDef());
        assertNull(matchingDef);
        assertTrue(configDef.hasMatchingSpecific(info.getFirstIPAddress()));
        assertNull(configDef.getConfigDef().getReadCommunity());
        mgr.mergeIntoConfig(info.createDef());
        matchingDef = mgr.findDefMatchingAttributes(info.createDef());
        assertNotNull(matchingDef);
        assertFalse(configDef.hasMatchingSpecific(info.getFirstIPAddress()));
        assertTrue(matchingDef.hasMatchingSpecific(info.getFirstIPAddress()));
        assertEquals(InetAddressUtils.toIpAddrLong(InetAddress.getByName("192.168.0.5")), InetAddressUtils.toIpAddrLong(InetAddress.getByName(matchingDef.getConfigDef().getSpecific(0))));
        assertEquals("abc", matchingDef.getConfigDef().getReadCommunity());
        assertEquals(1, SnmpPeerFactory.getSnmpConfig().getDefinitionCount());
    }
