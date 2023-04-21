    public static ELF readELF(String file) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(file));
        ByteArrayOutputStream baous = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        for (int read; (read = input.read(buf)) != -1; baous.write(buf, 0, read)) {
            ;
        }
        input.close();
        buf = null;
        byte[] data = baous.toByteArray();
        if (DEBUG) {
            System.out.println("Length of data: " + data.length);
        }
        ELF elf = new ELF(data);
        elf.readAll();
        return elf;
    }
