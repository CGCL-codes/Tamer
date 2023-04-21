    public static void copyByteStore(ByteStore src, ByteStore dest) throws FilingException {
        RfsInputStream input = (RfsInputStream) src.getOkiInputStream();
        RfsOutputStream output = (RfsOutputStream) dest.getOkiOutputStream();
        try {
            DataBlock dataBlock;
            while ((dataBlock = input.readMax()) != null) output.writeThrough(dataBlock);
        } finally {
            output.close();
            input.close();
        }
    }
