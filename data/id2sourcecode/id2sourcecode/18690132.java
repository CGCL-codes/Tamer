    public int openCurrent() throws FileNotFoundException, IOException {
        if (currentFile.exists() && currentFile.isFile() && currentFile.canRead()) {
            raf = new RandomAccessFile(currentFile.getAbsolutePath(), accessmode);
            inChannel = raf.getChannel();
            isOpen = true;
            if ((accessmode.equals("rw") || accessmode.equals("rwd") || accessmode.equals("rwd")) && currentFile.canWrite()) {
                axmode = MapMode.READ_WRITE;
                try {
                    lock = inChannel.tryLock();
                } catch (OverlappingFileLockException e) {
                }
            }
            if (accessmode.equals("r")) axmode = MapMode.READ_ONLY;
            capacity = (int) (inChannel.size() / bytesPerPixel);
            int size = (int) inChannel.size();
            fpointer = 0;
            fpointer = headerSize;
            buffer = inChannel.map(axmode, 0, size);
            buffer.order(bo);
            return buffer.limit();
        }
        return -1;
    }
