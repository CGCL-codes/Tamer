    @Parameters
    public static Collection<Object[]> data() throws ParseException {
        final Range range = new Range();
        range.setBegin("192.168.0.1");
        range.setEnd("192.168.0.255");
        final Definition def = new Definition();
        def.setVersion("v3");
        def.setReadCommunity("public");
        def.setWriteCommunity("private");
        def.addRange(range);
        def.addSpecific("192.168.1.1");
        def.addIpMatch("10.0.0.*");
        return Arrays.asList(new Object[][] { { def, "  <definition " + "    read-community=\"public\" " + "    write-community=\"private\" " + "    version=\"v3\">" + "    <range " + "      begin=\"192.168.0.1\" " + "      end=\"192.168.0.255\"/>" + "    <specific>192.168.1.1</specific>" + "    <ip-match>10.0.0.*</ip-match>" + "  </definition>\n", "target/classes/xsds/snmp-config.xsd" } });
    }
