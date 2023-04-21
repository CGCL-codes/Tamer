    public RandomFileAccessor(LocalRandomAccessFile file) throws IOException {
        super(file.getFile());
        this._file = file;
        this._file_size = file.getFile().length();
        RandomAccessFile raf = file.getRandomAccessFile();
        this._channel = raf.getChannel();
    }
