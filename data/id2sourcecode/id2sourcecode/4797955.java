    public TcpConnection(TcpConnectionContext aContext, ProtocolDecoderFactory aDecoderFactory, ProtocolEncoderFactory aEncoderFactory) throws IOException {
        super(aContext, aDecoderFactory, aEncoderFactory);
        mSelectorHandler = new Handler();
        mSocketChannel = aContext.getChannel();
        if (DEFULT_CAPACITY > 0) {
            mInBuffer = ByteBuffer.allocate(DEFULT_CAPACITY);
        } else {
            mInBuffer = ByteBuffer.allocate(mSocketChannel.socket().getReceiveBufferSize());
        }
        final Socket sock = mSocketChannel.socket();
        mRemoteAddress = sock.getInetAddress();
        mRemotePort = sock.getPort();
        mLocalAddress = sock.getLocalAddress();
        mLocalPort = sock.getLocalPort();
    }
