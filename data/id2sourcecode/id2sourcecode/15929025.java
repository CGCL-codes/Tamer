    public Multisample writeToFile(String pathFile) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(pathFile);
        FileChannel fc = fos.getChannel();
        header.writeToFileChannel(fc);
        ByteBuffer bb = ByteBuffer.allocate(0x0c);
        bb.put(0x09, (byte) zones.size());
        bb.rewind();
        fc.write(bb);
        for (int i = 0; i < zones.size(); i++) {
            zones.get(i).writeToFileChannel(fc);
        }
        fc.close();
        fos.close();
        return this;
    }
