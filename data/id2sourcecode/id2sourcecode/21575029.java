    public AudioWriter(File file, AudioFormat format) throws IOException {
        this.file = file;
        this.fis = new RandomAccessFile(file, "rw");
        this.fis.setLength(0);
        fis.seek(0);
        fis.write("RIFF".getBytes(), 0, 4);
        writeInt(36, fis);
        fis.write("WAVE".getBytes(), 0, 4);
        fis.write("fmt ".getBytes(), 0, 4);
        writeInt(0x10, fis);
        writeShort(1, fis);
        writeShort(nChannel = format.getChannels(), fis);
        writeInt((int) (format.getFrameRate()), fis);
        writeInt((int) (format.getFrameRate()) * format.getChannels() * 2, fis);
        writeShort((short) format.getChannels() * 2, fis);
        writeShort(16, fis);
        fis.write("data".getBytes(), 0, 4);
        writeInt(0, fis);
    }
