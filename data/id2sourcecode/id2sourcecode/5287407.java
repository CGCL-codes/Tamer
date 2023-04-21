    protected BinaryValueFromFile(BinaryValueManager manager, BinaryValueType binaryValueType, File file) throws XPathException {
        super(manager, binaryValueType);
        try {
            this.file = file;
            this.channel = new RandomAccessFile(file, "r").getChannel();
            this.buf = channel.map(MapMode.READ_ONLY, 0, channel.size());
        } catch (IOException ioe) {
            throw new XPathException(ioe.getMessage(), ioe);
        }
    }
