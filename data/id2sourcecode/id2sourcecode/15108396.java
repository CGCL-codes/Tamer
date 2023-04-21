    public static void sshd(int port) throws IOException {
        boolean keepRunning = true;
        ServerSocket listenSock = null;
        SSHRSAKeyFile keyFile;
        keyFile = new SSHRSAKeyFile(hostKeyFile);
        hostKey = new KeyPair(keyFile.getPublic(), getPrivate(keyFile));
        listenSock = new ServerSocket(port);
        int hostKeyBits = ((RSAPublicKey) hostKey.getPublic()).getModulus().bitLength();
        int keyLenDiff = Math.abs(serverKeyBits - hostKeyBits);
        if (keyLenDiff < 24) {
            throw new IOException("Invalid server keys, difference in sizes must be at least 24 bits");
        }
        System.out.print("generating server-key of length " + serverKeyBits + "...");
        try {
            serverKey = generateKeyPair("RSA", serverKeyBits);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Can't generate RSA key: " + e.getMessage());
        }
        System.out.println("done");
        System.out.println("starting new MindTunnel on port " + port + "...");
        while (keepRunning) {
            Socket sshSocket = listenSock.accept();
            SSHServer srv = new SSHServer(sshSocket, PROTOFLAG_HOST_IN_FWD_OPEN, 0xff, 0x0f, serverKey, hostKey);
            srv.localAddr = InetAddress.getLocalHost();
            srv.start();
        }
    }
