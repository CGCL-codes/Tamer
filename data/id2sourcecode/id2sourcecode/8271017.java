        EditLogFileOutputStream(File name) throws IOException {
            super();
            file = name;
            bufCurrent = new DataOutputBuffer(sizeFlushBuffer);
            bufReady = new DataOutputBuffer(sizeFlushBuffer);
            RandomAccessFile rp = new RandomAccessFile(name, "rw");
            fp = new FileOutputStream(rp.getFD());
            fc = rp.getChannel();
            fc.position(fc.size());
        }
