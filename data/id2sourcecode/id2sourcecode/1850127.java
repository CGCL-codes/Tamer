    protected synchronized void Decode() throws DecodeFailedException, IOException {
        BlockCipher e = C.KM.getSymmetricEngine();
        CBCBlockCipher c = new CBCBlockCipher(e);
        c.init(false, Head.SymKey.getParameters());
        CryptoClientTool tool = new CryptoClientTool(c);
        BytesChannel in = new BytesChannel(EncodedHeader);
        WriteBytesChannel out = new WriteBytesChannel();
        ChannelReader reader = new ChannelReader(in);
        ChannelWriter writer = new ChannelWriter(out);
        tool.Process(reader, writer);
        writer.close();
        DecodedHeader = out.getBytes();
    }
