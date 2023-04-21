    public void init(List<DataChannel> channels, double startTime, double endTime, File file) throws IOException {
        this.file = file;
        fileWriter = new RandomAccessFile(file, "rw");
        fileWriter.setLength(0);
        fileWriter.writeBytes(HEADER_TEXT_FIELD);
        fileWriter.writeBytes(new Date().toString());
        while (fileWriter.getFilePointer() < 124) {
            fileWriter.write(' ');
        }
        bb.clear();
        bb.putShort((short) 0x0100);
        bb.put("IM".getBytes());
        bb.flip();
        fileWriter.getChannel().write(bb);
        writeDoubleArrayHeader("data", channels.size());
    }
