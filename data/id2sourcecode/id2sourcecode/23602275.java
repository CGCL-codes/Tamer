    public NIOScaledRAFile(String name, boolean mode) throws FileNotFoundException, IOException {
        super(name, mode);
        if (super.length() > MAX_NIO_LENGTH) {
            Trace.printSystemOut("Initiatiated without nio");
            return;
        }
        wasNio = isNio = true;
        channel = file.getChannel();
        enlargeBuffer(super.length(), 0);
        Trace.printSystemOut("initial length " + super.length());
        Trace.printSystemOut("NIO file instance created. mode:  " + mode);
    }
