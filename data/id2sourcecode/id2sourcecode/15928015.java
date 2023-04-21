    public NetworkManager(Socket socket, String name, NetworkHandler nethandler) throws IOException {
        sendQueueLock = new Object();
        isRunning = true;
        readPackets = Collections.synchronizedList(new ArrayList());
        dataPackets = Collections.synchronizedList(new ArrayList());
        networkSocket = socket;
        remoteAddress = socket.getRemoteSocketAddress();
        lastReadAgo = 0;
        queueByteLength = 0;
        this.nethandler = nethandler;
        try {
            socket.setSoTimeout(30000);
            socket.setTrafficClass(24);
        } catch (SocketException se) {
            System.err.println(se.getMessage());
        }
        socketInputStream = new DataInputStream(socket.getInputStream());
        socketOutputStream = new DataOutputStream(socket.getOutputStream());
        readThread = new ReaderThread(this, name + " reader thread");
        writeThread = new WriterThread(this, name + " writer thread");
        readThread.start();
        writeThread.start();
    }
