    public CompleteFileBlockProvider(int blockNumber, Hash root, CoreSubsystem core) throws IOException {
        super(blockNumber, root, core);
        File f = new File(fd.getFullPath());
        if (!f.exists()) throw new FileNotFoundException("Could not find: " + fd + " in complete files!");
        FileInputStream in = new FileInputStream(f);
        fileChannel = in.getChannel();
        fileChannel.position(((long) blockNumber) * BLOCK_SIZE);
        if (T.t) T.ass(fileChannel.isOpen(), "FileChannel closed!");
        if (T.t) T.trace("Ready to start sending block");
    }
