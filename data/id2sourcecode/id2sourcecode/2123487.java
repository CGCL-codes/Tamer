    public NIOScaledRAFile(String name, boolean mode, int multiplier) throws FileNotFoundException, IOException {
        super(name, mode, multiplier);
        if (super.length() > (1 << 30)) {
            Trace.printSystemOut("Initiatiated without nio");
            return;
        }
        isNio = true;
        channel = file.getChannel();
        enlargeBuffer(super.length(), 0);
        isNio = true;
        Trace.printSystemOut("NIO file instance created. mode:  " + mode);
    }
