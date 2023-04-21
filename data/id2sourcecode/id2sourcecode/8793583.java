    @Test
    public void testGetConnection_1() throws Exception {
        ConnectionPacketLink fixture = new ConnectionPacketLink(new RpcConnection(1L, SocketChannel.open(), new PacketManager(), (SelectionKey) null, null), new Packet(new byte[] {}));
        RpcConnection result = fixture.getConnection();
        assertNotNull(result);
        assertEquals("{id: 1, protocol: RPC, version: -1, last contact: 1304709029557, packet counter: {read: 0, write: 0}, pending packets: 0, address: Socket[unconnected]}", result.toString());
        assertEquals(1L, result.getId());
        assertEquals((byte) -1, result.getVersion());
        assertEquals(null, result.getRemoteAddr());
        assertEquals(0, result.pendingPacketCount());
        assertEquals(1304709029557L, result.getCreationTime());
        assertEquals(1304709029557L, result.getLastContactTime());
        assertEquals(false, result.isTimeOut());
    }
