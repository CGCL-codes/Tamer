    public void run() {
        try {
            _serverSocket = new ServerSocket(this.ISECURITYMANAGER_PORT);
            serverKeyStore = KeyStore.getInstance("JKS");
            serverKeyStore.load(new FileInputStream(this.ISECURITYMANAGER_KEYSTORE + ".ks"), ISECURITYMANAGER_KEYSTORE_PASSWORD);
            KeyPairGenerator kg = KeyPairGenerator.getInstance("DSA");
            kg.initialize(1024);
            KeyPair kp = kg.generateKeyPair();
            privateKey = kp.getPrivate();
            publicKey = kp.getPublic();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(serverKeyStore, ISECURITYMANAGER_KEYSTORE_PASSWORD);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(serverKeyStore);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            socketFactory = sslContext.getSocketFactory();
            while (true) {
                Socket socket = _serverSocket.accept();
                if (socket.getInetAddress().equals(socket.getLocalAddress())) {
                    iSecurityManagerThread thread = new iSecurityManagerThread(this, socket);
                    thread.start();
                } else {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not listen on port: " + ISECURITYMANAGER_PORT);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
