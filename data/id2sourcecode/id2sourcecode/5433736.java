    private int readUnsignedInt(C c, int value) {
        IntArrayBitWriter writer = new IntArrayBitWriter(100);
        IntArrayBitReader reader = new IntArrayBitReader(writer.getInts(), writer.getSize());
        c.encodeInt(writer, value);
        writer.flush();
        return c.decodePositiveInt(reader);
    }
