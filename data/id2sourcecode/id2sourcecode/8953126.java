    public WavInputStream(URL resource) throws IOException {
        super(resource, -1);
        dataIn = new DataInputStream(in);
        if (dataIn.readInt() != RIFFid) throw (new IOException("Not a valid RIFF file"));
        int firstByte = (0x000000FF & dataIn.readByte());
        int secondByte = (0x000000FF & dataIn.readByte());
        int thirdByte = (0x000000FF & dataIn.readByte());
        int fourthByte = (0x000000FF & dataIn.readByte());
        fileSize = 8L + (firstByte << 0 | secondByte << 8 | thirdByte << 16 | fourthByte << 24) & 0xFFFFFFFFL;
        if (dataIn.readInt() != WAVEid) throw (new IOException("Not a valid WAVE file"));
        headerSize += 12;
        seekAudio();
        setLength((fileSize - headerSize) * 8f / (getChannelCount() * getBitRate() * getDepth()));
    }
