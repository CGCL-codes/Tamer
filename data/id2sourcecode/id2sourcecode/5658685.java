    public SSLSocket(CacheClientManager manager, String host, int writeBufferSize, int timeout, int connectTimeout, boolean noDelay) throws IOException, UnknownHostException, KeyManagementException, NoSuchAlgorithmException {
        this.manager = manager;
        this.host = host;
        String[] ip = host.split(":");
        if (ip.length >= 2) {
            socket = createSocket(ip[0].trim(), Integer.parseInt(ip[1].trim()), connectTimeout);
        } else {
            socket = createSocket(ip[0].trim(), manager.getDefaultPort(), connectTimeout);
        }
        readBuffer = ByteBuffer.allocateDirect(8 * 1024);
        readBuffer.flip();
        writeBuffer = ByteBuffer.allocateDirect(writeBufferSize);
        if (timeout >= 0) {
            socket.setSoTimeout(timeout);
        }
        socket.setTcpNoDelay(noDelay);
        socketChannel = socket.getChannel();
        if (socketChannel == null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
            throw new IOException("Can not getChannel for host:" + host);
        }
        initSSL(timeout);
    }
