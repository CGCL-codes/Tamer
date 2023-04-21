    public int openCurrent() throws FileNotFoundException, IOException {
        if (debug) System.out.println(currentFile.getAbsolutePath());
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
            if (debug) System.out.println("size " + size);
            fpointer = 0;
            if (currentFile.equals(headerFile)) fpointer = headerSize;
            buffer = inChannel.map(axmode, 0, size);
            buffer.order(bo);
            return buffer.limit();
        }
        return -1;
    }
