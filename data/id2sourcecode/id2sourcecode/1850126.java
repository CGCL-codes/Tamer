    protected synchronized void Encode() throws EncodeFailedException, IOException {
        BlockCipher e = C.KM.getSymmetricEngine();
        CBCBlockCipher c = new CBCBlockCipher(e);
        c.init(true, Head.SymKey.getParameters());
        CryptoClientTool tool = new CryptoClientTool(c);
        BytesChannel in = new BytesChannel(DecodedHeader);
        WriteBytesChannel out = new WriteBytesChannel();
        ChannelReader reader = new ChannelReader(in);
        ChannelWriter writer = new ChannelWriter(out);
        tool.Process(reader, writer);
        writer.close();
        EncodedHeader = out.getBytes();
        EncodedLength = EncodedHeader.length;
    }
