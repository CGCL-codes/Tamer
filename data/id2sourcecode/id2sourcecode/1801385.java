    public synchronized void setBytes(byte[] bytes) {
        BytesChannel bc = new BytesChannel(bytes);
        ChannelReader cr = new ChannelReader(bc);
        try {
            getChannel().Read(cr);
        } catch (IOException e) {
            getChannel().setParameters(null);
            e.printStackTrace();
        }
    }
