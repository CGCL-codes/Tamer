    @Test
    public void testPacketCounter_1() throws Exception {
        PacketCounter result = new PacketCounter();
        assertNotNull(result);
        assertEquals("{read: 0, write: 0}", result.toString());
        assertEquals(0L, result.readCount());
        assertEquals(0L, result.writeCount());
    }
