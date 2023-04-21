    public boolean onHandshakeRecieved(WebSocket conn, String handshake, byte[] reply) throws IOException, NoSuchAlgorithmException {
        if (this.draft == WebSocketDraft.DRAFT76) {
            if (reply == null) {
                return false;
            }
            byte[] challenge = new byte[] { (byte) (this.number1 >> 24), (byte) ((this.number1 << 8) >> 24), (byte) ((this.number1 << 16) >> 24), (byte) ((this.number1 << 24) >> 24), (byte) (this.number2 >> 24), (byte) ((this.number2 << 8) >> 24), (byte) ((this.number2 << 16) >> 24), (byte) ((this.number2 << 24) >> 24), this.key3[0], this.key3[1], this.key3[2], this.key3[3], this.key3[4], this.key3[5], this.key3[6], this.key3[7] };
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] expected = md5.digest(challenge);
            for (int i = 0; i < reply.length; i++) {
                if (expected[i] != reply[i]) {
                    return false;
                }
            }
        }
        return true;
    }
