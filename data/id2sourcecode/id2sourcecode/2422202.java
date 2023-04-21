    public void loadFile(int type, String name) throws Exception {
        InputStream in = openFile(name);
        try {
            byte[] header = new byte[24];
            int len = in.read(header);
            if (len != 24) vzFileException(null);
            for (int i = 0; i < 18; i++) memory.writeByte(0x7ab2 + i, header[i + 4] & 0xff);
            type = header[21] & 0xff;
            memory.writeByte(0x7ad2, type);
            int start = (header[22] & 0xff) + 256 * (header[23] & 0xff);
            memory.writeByte(0x781e, header[22] & 0xff);
            memory.writeByte(0x781f, header[23] & 0xff);
            memory.writeByte(0x7839, 0x63);
            memory.writeByte(0x787d, 0xc9);
            int address = start;
            int read;
            do {
                read = in.read();
                if (read != -1) {
                    memory.writeByte(address, read);
                    address = (address + 1) & 0xffff;
                }
            } while (read != -1);
            in.close();
            memory.writeByte(0x78f9, address & 0xff);
            memory.writeByte(0x78fa, (address >> 8) & 0xff);
            memory.writeByte(0xffc9, 0x1e);
            memory.writeByte(0xffca, 0x1d);
            z80.setSP(0xffc9);
            z80.setPC(0x36be);
        } finally {
            in.close();
        }
    }
