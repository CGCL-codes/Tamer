    private boolean connect() {
        Debug.log("[Servent] Connection to " + ip + ":" + port);
        ReadLineReader rlr;
        try {
            socket = new Socket();
            socket.connect(new java.net.InetSocketAddress(ip, port), Constants.CONNECT_SOCKET_TIMEOUT);
            socket.setSoTimeout(Constants.SOCKET_SO_TIMEOUT);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            writerThread.setOutputStream(out);
            readerThread.setInputStream(in);
        } catch (Exception e) {
            return false;
        }
        LineWriter lw = new LineWriter(out);
        rlr = new ReadLineReader(in);
        try {
            lw.println(NETWORK_NAME + LOGINMSG_06);
            lw.println(USER_AGENT + ": " + VENDORCODE);
            lw.println("X-Ultrapeer: False");
            lw.println("X-Query-Routing: 0.1");
            lw.println();
            lw.flush();
        } catch (IOException ie) {
            Debug.log("gnutella: Could not handshake");
            return false;
        }
        String response = rlr.readLine();
        if ((response != null) && (response.toUpperCase().startsWith(NETWORK_NAME + "/" + VERSION))) {
            boolean result = (response.indexOf("200") != -1);
            Debug.log("Response: " + response);
            String line;
            while ((line = rlr.readLine()) != null) {
                Debug.log(line);
                if (line.toUpperCase().indexOf("X-TRY") != -1) {
                    String lineAddrs = line.substring(line.indexOf(':') + 1);
                    String addrs[] = lineAddrs.split(",");
                    for (int i = 0; i < addrs.length; i++) providerServentsAddress.pushAddress(addrs[i].trim());
                }
            }
            if (result) {
                try {
                    lw.println(NETWORK_NAME + "/" + VERSION + " 200 OK");
                    lw.println();
                    lw.flush();
                } catch (IOException ie) {
                    Debug.log("gnutella: Could not handshake");
                    return false;
                }
            }
            return result;
        } else {
            Debug.log("servent response: " + response);
            return false;
        }
    }
