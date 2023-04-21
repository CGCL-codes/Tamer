    public void loadFile(int type, String name) throws Exception {
        if ((zx81 && (name.toLowerCase().endsWith(".p") || name.endsWith(".81"))) || (!zx81 && (name.toLowerCase().endsWith(".o") || name.endsWith(".80")))) {
            InputStream in = openFile(name);
            int addr = zx81 ? 0x4009 : 0x4000;
            try {
                int read;
                while ((read = in.read()) != -1) memory.writeByte(addr++, read);
                while (addr < 0x7ffe) memory.writeByte(addr++, 0x00);
                z80.setIY(0x4000);
                if (zx81) {
                    int[] data = { 0xff, 0x80, 0xfc, 0x7f, 0x00, 0x80, 0x00, 0xfe, 0xff };
                    for (int i = 0; i < data.length; i++) memory.writeByte(i + 0x4000, data[i]);
                    memory.writeByte(0x7ffc, 0x76);
                    memory.writeByte(0x7ffd, 0x06);
                    z80.setSP(0x7ffc);
                    z80.setPC(0x0207);
                    z80.setIX(0x028f);
                    z80.setI(0x1e);
                    renderer.writePort(0xfd, 0x00);
                } else {
                    z80.setSP(0x7ffe);
                    z80.setPC(0x0283);
                    z80.setI(0x0e);
                }
                z80.di();
            } finally {
                in.close();
            }
        }
    }
