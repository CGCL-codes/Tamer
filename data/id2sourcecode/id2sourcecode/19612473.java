    public User(Socket socket) {
        try {
            this.socket = socket;
            byte[] digest = md.digest((md5salt + this.socket.toString()).getBytes());
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            this.md5 = hexString.toString();
            hexString.setLength(0);
            this.name = "addr=" + this.socket.getInetAddress().toString() + ":" + this.socket.getPort();
            this.error = false;
            this.io = new MessageFactory(15000);
            this.chatrooms = new Vector<Chatroom>();
            this.io.write(this.md5, MessageFactory.LOGIN);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            this.kill();
        }
    }
