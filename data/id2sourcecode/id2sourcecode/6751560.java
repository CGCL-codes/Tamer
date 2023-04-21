    public void run() throws Exception {
        Channel c = AppContext.getChannelManager().getChannel(Server.allChannelName);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            dos.writeByte(IOConstants.serverClose);
            c.send(null, buffer);
        } catch (IOException e) {
        }
        System.exit(0);
    }
