    public void connect(File file, int size) throws IPCException {
        if (null != this.byteBuffer) {
            throw new IPCException(Errors.AlreadyConnected);
        }
        myFileCreator = Utils.checkFile(file);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            throw new IPCException(Errors.ErrorOpeningMappingFile, e);
        }
        channel = raf.getChannel();
        try {
            this.byteBuffer = channel.map(MapMode.READ_WRITE, 0, size);
        } catch (IOException e) {
            throw new IPCException(Errors.ErrorCreatingMap, e);
        }
        this.segmentFile = file;
        this.size = size;
    }
