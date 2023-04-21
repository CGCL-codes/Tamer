    public ObjectWriter(Object obj) throws IOException {
        size = 0;
        Writer writer = new Writer(obj);
        InputStream is = writer.getInputStream();
        writer.start();
        boolean over = false;
        buffer = new LinkedList();
        int space = buffSize;
        int writeOffset = 0;
        byte lastBuff[] = new byte[buffSize];
        while (!over) {
            int read = is.read(lastBuff, writeOffset, space);
            if (read == -1) {
                lastOffset = writeOffset;
                buffer.addLast(lastBuff);
                over = true;
            } else {
                space -= read;
                size += read;
                if (space == 0) {
                    buffer.addLast(lastBuff);
                    space = buffSize;
                    writeOffset = 0;
                    lastBuff = new byte[buffSize];
                } else {
                    writeOffset += read;
                }
            }
        }
        ;
        outputStream = new PipedOutputStream();
        inputStream = new PipedInputStream(outputStream);
    }
