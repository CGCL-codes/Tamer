    public MasterServerLink(String name, String password, boolean newClient) throws IOException, PZException {
        masterAddress = new InetSocketAddress(MASTER_HOST, MASTER_PORT);
        buffer = ByteBuffer.allocate(1000);
        udpBuffer = ByteBuffer.allocate(13);
        this.name = name;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            d(ex);
            throw new PZException("Unable to use MD5", ex);
        }
        this.password = digest.digest(("$@ö\\<!>" + password + "{ñ=?]£#").getBytes());
        if (newClient) {
            put(LINK_NEW_CLIENT, buffer);
            put(name, buffer);
            buffer.put(this.password);
            writeAndRead();
            switch(getCommand(buffer)) {
                case MASTER_CLIENT_CREATED:
                    client = new Client(buffer.getInt(), buffer.getInt(), name, this);
                    connected = true;
                    break;
                case MASTER_NAME_TAKEN:
                    throw new PZException("Name taken");
                case MASTER_FAILED_TO_PARSE:
                default:
                    throw new PZException("Failed to parse");
            }
        } else {
            generateMD5();
            put(LINK_LOGIN_WITH_NAME, buffer);
            put(name, buffer);
            buffer.put(md5);
            writeAndRead();
            switch(getCommand(buffer)) {
                case ACCEPTED:
                    client = new Client(buffer.getInt(), buffer.getInt(), name, this);
                    connected = true;
                    break;
                case DENIED:
                    throw new PZException("Incorrect name or password");
                case MASTER_FAILED_TO_PARSE:
                default:
                    throw new PZException("Failed to parse");
            }
        }
        int count = 0;
        do {
            if (count++ > 30) throw new PZException("Failed to confirm udp");
            clientUdp();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            client.update();
        } while (!client.udpConfirmed());
        d("Client connected in " + count + (count == 1 ? "try." : " tries."));
    }
