    @Test
    public void testPutSendReceiveGetXmlString() throws Exception {
        if (XMPP_ACCOUNT1 != null) {
            ConnectionConfiguration cc = new ConnectionConfiguration(XMPP_ADDRESS1, Integer.valueOf(XMPP_PORT1));
            XMPPConnection conn = new XMPPConnection(cc);
            conn.connect();
            conn.login(XMPP_LOGIN1, XMPP_PASSWORD1);
            Message packet = new Message(XMPP_ACCOUNT2);
            packet.setFrom(XMPP_ACCOUNT1);
            packet.setBody("Test");
            MapperFactory factory = new SimpleMapperFactory();
            Mapper<TestStringDocument> instance = factory.createInstance(TestStringDocument.class);
            instance.addExtension(packet, testXmlString1);
            conn.sendPacket(packet);
            conn.disconnect();
            ConnectionConfiguration cc2 = new ConnectionConfiguration(XMPP_ADDRESS2, Integer.valueOf(XMPP_PORT2));
            XMPPConnection conn2 = new XMPPConnection(cc2);
            conn2.connect();
            PacketCollector pc = conn2.createPacketCollector(new PacketTypeFilter(Message.class));
            conn2.login(XMPP_LOGIN2, XMPP_PASSWORD2);
            Packet p = pc.nextResult();
            TestStringDocument result = instance.getExtension(p);
            assertEquals(TEST_STRING1, result.getTestString());
            conn2.disconnect();
        }
    }
