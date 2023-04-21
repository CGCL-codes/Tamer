    @Test
    public void testToString_1() throws Exception {
        RpcConnection fixture = new RpcConnection(1L, SocketChannel.open(), new PacketManager(), (SelectionKey) null, null);
        String result = fixture.toString();
        assertEquals("{id: 1, protocol: RPC, version: -1, last contact: 1304707562104, packet counter: {read: 0, write: 0}, pending packets: 0, address: Socket[unconnected]}", result);
    }
