    public void open(File f, String mode) throws IOException {
        if (debug) System.out.println(f.getAbsolutePath());
        if (!(mode.equals("r") || mode.equals("rw") || mode.equals("rwd") || mode.equals("rws"))) throw new IllegalArgumentException("not a valid opening mode");
        if (f.exists() && f.isFile() && f.canRead()) {
            currentFile = f;
            if (files.add(f)) nFiles++;
            raf = new RandomAccessFile(f.getAbsolutePath(), mode);
            inChannel = raf.getChannel();
            isOpen = true;
            if ((mode.equals("rw") || mode.equals("rwd") || mode.equals("rwd")) && f.canWrite()) {
                axmode = MapMode.READ_WRITE;
                try {
                    lock = inChannel.tryLock();
                } catch (OverlappingFileLockException e) {
                }
            }
            if (mode.equals("r")) axmode = MapMode.READ_ONLY;
        }
    }
