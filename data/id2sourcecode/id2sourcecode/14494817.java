    public AudioClipReader(File clipFile, long startFrame) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(clipFile, "r");
        this.reader = new DAudioReader(raf);
        reader.seekFrame(0);
        this.nch = reader.getChannels();
        System.out.println(" Channels = " + nch);
        this.startFrame = startFrame;
    }
