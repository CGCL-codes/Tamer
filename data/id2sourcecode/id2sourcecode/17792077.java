    @Test
    public void testRpcConnection_1() throws Exception {
        long id = 1L;
        SocketChannel clientChannel = SocketChannel.open();
        PacketManager packetManager = new PacketManager();
        SelectionKey selectionKey = null;
        RpcConnection result = new RpcConnection(id, clientChannel, packetManager, selectionKey, null);
        assertNotNull(result);
        assertEquals("{id: 1, protocol: RPC, version: -1, last contact: 1304707560781, packet counter: {read: 0, write: 0}, pending packets: 0, address: Socket[unconnected]}", result.toString());
        assertEquals(1L, result.getId());
        assertEquals((byte) -1, result.getVersion());
        assertEquals(null, result.getRemoteAddr());
        assertEquals(0, result.pendingPacketCount());
        assertEquals(1304707560781L, result.getCreationTime());
        assertEquals(1304707560781L, result.getLastContactTime());
        assertEquals(false, result.isTimeOut());
    }
