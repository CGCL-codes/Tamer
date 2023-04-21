    public boolean onHandshakeRecieved(WebSocket conn, String handshake, byte[] key3) throws IOException, NoSuchAlgorithmException {
        if (FLASH_POLICY_REQUEST.equals(handshake)) {
            String policy = getFlashSecurityPolicy() + "\0";
            conn.socketChannel().write(ByteBuffer.wrap(policy.getBytes(WebSocket.UTF8_CHARSET)));
            return false;
        }
        String[] requestLines = handshake.split("\r\n");
        boolean isWebSocketRequest = true;
        String line = requestLines[0].trim();
        String path = null;
        if (!(line.startsWith("GET") && line.endsWith("HTTP/1.1"))) {
            isWebSocketRequest = false;
        } else {
            String[] firstLineTokens = line.split(" ");
            path = firstLineTokens[1];
        }
        Properties p = new Properties();
        for (int i = 1; i < requestLines.length; i++) {
            line = requestLines[i];
            int firstColon = line.indexOf(":");
            if (firstColon != -1) {
                p.setProperty(line.substring(0, firstColon).trim(), line.substring(firstColon + 1).trim());
            }
        }
        String prop = p.getProperty("Upgrade");
        if (prop == null || !prop.equals("WebSocket")) {
            isWebSocketRequest = false;
        }
        prop = p.getProperty("Connection");
        if (prop == null || !prop.equals("Upgrade")) {
            isWebSocketRequest = false;
        }
        String key1 = p.getProperty("Sec-WebSocket-Key1");
        String key2 = p.getProperty("Sec-WebSocket-Key2");
        String headerPrefix = "";
        byte[] responseChallenge = null;
        switch(this.draft) {
            case DRAFT75:
                if (key1 != null || key2 != null || key3 != null) {
                    isWebSocketRequest = false;
                }
                break;
            case DRAFT76:
                if (key1 == null || key2 == null || key3 == null) {
                    isWebSocketRequest = false;
                }
                break;
        }
        if (isWebSocketRequest) {
            if (key1 != null && key2 != null && key3 != null) {
                headerPrefix = "Sec-";
                byte[] part1 = this.getPart(key1);
                byte[] part2 = this.getPart(key2);
                byte[] challenge = new byte[16];
                challenge[0] = part1[0];
                challenge[1] = part1[1];
                challenge[2] = part1[2];
                challenge[3] = part1[3];
                challenge[4] = part2[0];
                challenge[5] = part2[1];
                challenge[6] = part2[2];
                challenge[7] = part2[3];
                challenge[8] = key3[0];
                challenge[9] = key3[1];
                challenge[10] = key3[2];
                challenge[11] = key3[3];
                challenge[12] = key3[4];
                challenge[13] = key3[5];
                challenge[14] = key3[6];
                challenge[15] = key3[7];
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                responseChallenge = md5.digest(challenge);
            }
            String responseHandshake = "HTTP/1.1 101 Web Socket Protocol Handshake\r\n" + "Upgrade: WebSocket\r\n" + "Connection: Upgrade\r\n";
            responseHandshake += headerPrefix + "WebSocket-Origin: " + p.getProperty("Origin") + "\r\n";
            responseHandshake += headerPrefix + "WebSocket-Location: ws://" + p.getProperty("Host") + path + "\r\n";
            if (p.containsKey(headerPrefix + "WebSocket-Protocol")) {
                responseHandshake += headerPrefix + "WebSocket-Protocol: " + p.getProperty("WebSocket-Protocol") + "\r\n";
            }
            if (p.containsKey("Cookie")) {
                responseHandshake += "Cookie: " + p.getProperty("Cookie") + "\r\n";
            }
            responseHandshake += "\r\n";
            conn.socketChannel().write(ByteBuffer.wrap(responseHandshake.getBytes()));
            if (responseChallenge != null) {
                conn.socketChannel().write(ByteBuffer.wrap(responseChallenge));
            }
            return true;
        }
        return false;
    }
