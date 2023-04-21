    public final void testWritePropertyDestinationIntIntIntIntByteArray() throws KNXException {
        final byte[] read = mc.readProperty(dco2, 0, 51, 1, 1);
        mc.writeProperty(dco2, 0, 51, 1, 1, new byte[] { 7 });
        final byte[] read2 = mc.readProperty(dco2, 0, 51, 1, 1);
        assertTrue(Arrays.equals(new byte[] { 7 }, read2));
        mc.writeProperty(dco2, 0, 51, 1, 1, read);
    }
