    public final void testWriteAddressByteArrayIndividualAddress() throws KNXException {
        final byte[] sno = new byte[] { 0x00, 0x01, 0x00, 0x11, (byte) 0xcb, 0x08 };
        final IndividualAddress write = mc.readAddress(sno);
        mc.writeAddress(sno, write);
        final IndividualAddress read = mc.readAddress(sno);
        assertEquals(write, read);
        final IndividualAddress write2 = new IndividualAddress(3, 0, 2);
        mc.writeAddress(sno, write2);
        final IndividualAddress read2 = mc.readAddress(sno);
        assertEquals(write2, read2);
        mc.writeAddress(sno, write);
    }
