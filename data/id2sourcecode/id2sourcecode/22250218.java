    @Test
    public void testToString_1() throws Exception {
        PacketCounter fixture = new PacketCounter();
        fixture.readOne();
        fixture.writeOne();
        String result = fixture.toString();
        assertEquals("{read: 1, write: 1}", result);
    }
