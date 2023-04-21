    public Logger(File file) throws FileNotFoundException, IOException {
        super(Logger._fos = new FileOutputStream(file, true));
        this._file_channel = Logger._fos.getChannel();
        this._pid = PID.getPIDString();
    }
