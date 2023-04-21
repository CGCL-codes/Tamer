    public NetworkManager(Socket par1Socket, String par2Str, NetHandler par3NetHandler) throws IOException {
        sendQueueLock = new Object();
        isRunning = true;
        readPackets = Collections.synchronizedList(new ArrayList());
        dataPackets = Collections.synchronizedList(new ArrayList());
        chunkDataPackets = Collections.synchronizedList(new ArrayList());
        isServerTerminating = false;
        isTerminating = false;
        terminationReason = "";
        timeSinceLastRead = 0;
        sendQueueByteLength = 0;
        chunkDataSendCounter = 0;
        field_20100_w = 50;
        networkSocket = par1Socket;
        remoteSocketAddress = par1Socket.getRemoteSocketAddress();
        netHandler = par3NetHandler;
        try {
            par1Socket.setSoTimeout(30000);
            par1Socket.setTrafficClass(24);
        } catch (SocketException socketexception) {
            System.err.println(socketexception.getMessage());
        }
        socketInputStream = new DataInputStream(par1Socket.getInputStream());
        socketOutputStream = new DataOutputStream(new BufferedOutputStream(par1Socket.getOutputStream(), 5120));
        readThread = new NetworkReaderThread(this, (new StringBuilder()).append(par2Str).append(" read thread").toString());
        writeThread = new NetworkWriterThread(this, (new StringBuilder()).append(par2Str).append(" write thread").toString());
        readThread.start();
        writeThread.start();
    }
