    public static Multisample createFromFile(String pathFile) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(pathFile);
        FileChannel fc = fis.getChannel();
        Multisample multi = new Multisample("", "", "");
        multi.header.readFromFileChannel(fc);
        ByteBuffer bb = ByteBuffer.allocate(0x0c);
        fc.read(bb);
        for (int i = 0; i < bb.get(0x09); i++) {
            multi.zones.add((Zone) new Zone().readFromFileChannel(fc));
        }
        fc.close();
        fis.close();
        return multi;
    }
