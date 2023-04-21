    public void connect() throws IOException {
        clientSocket = socket.accept();
        if (logger.isDebugEnabled()) logger.debug("Accepted socket: " + clientSocket.getLocalAddress() + ":" + clientSocket.getLocalPort());
        Reader reader = new InputStreamReader(clientSocket.getInputStream());
        StringWriter writer = new StringWriter();
        int count;
        char[] buffer = new char[8 * 1024];
        while ((count = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, count);
            if (writer.toString().indexOf('\n') != -1) break;
        }
        if (logger.isTraceEnabled()) logger.trace("Read '" + writer.toString().trim() + "' from the socket.");
        String readInfo = writer.toString().substring(0, writer.toString().indexOf('\n'));
        String remoteIPString = readInfo.substring(0, readInfo.indexOf('|'));
        String remotePortString = readInfo.substring(readInfo.indexOf('|') + 1, readInfo.length() - 1);
        otherEndHostIP = InetAddress.getByAddress(ByteArrayUtil.decodeFromHex(remoteIPString));
        otherEndPort = Integer.parseInt(remotePortString);
    }
