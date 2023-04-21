    private void testPrimitiveAccess(IoRelativeWriter write, IoRelativeReader read) {
        byte b = (byte) 0x12;
        write.put(b);
        assertEquals(b, read.get());
        short s = (short) 0x12;
        write.putShort(s);
        assertEquals(s, read.getShort());
        int i = 0x12345678;
        write.putInt(i);
        assertEquals(i, read.getInt());
        long l = 0x1234567890123456L;
        write.putLong(l);
        assertEquals(l, read.getLong());
        float f = Float.intBitsToFloat(i);
        write.putFloat(f);
        assertEquals(f, read.getFloat(), 0);
        double d = Double.longBitsToDouble(l);
        write.putDouble(d);
        assertEquals(d, read.getDouble(), 0);
        char c = (char) 0x1234;
        write.putChar(c);
        assertEquals(c, read.getChar());
    }
