    @Override
    protected void loadBuffer() throws IOException {
        if (buffer == null) throw new NullPointerException("Buffer is null, has reader been opened?");
        String fileName = streamConfig.getStreamFileName(bufferStartTime_s);
        FileInputStream fis = new FileInputStream(fileName);
        fc = fis.getChannel();
        endPos = fc.size();
        bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, endPos);
        for (int i = 0; i < buffer.length; i++) {
            DisplayCachedNetState dbuffer = (DisplayCachedNetState) buffer[i];
            dbuffer.pos = -1;
            dbuffer.myReader = this;
        }
        buffer[0].setState();
    }
