    public static void copyByteStoreOld(ByteStore src, ByteStore dest) throws FilingException {
        OkiInputStream input = src.getOkiInputStream();
        OkiOutputStream output = dest.getOkiOutputStream();
        try {
            int len = 0;
            byte[] bytearr = new byte[8192];
            while ((len = input.read(bytearr)) != -1) output.write(bytearr, 0, len);
        } finally {
            output.close();
            input.close();
        }
    }
