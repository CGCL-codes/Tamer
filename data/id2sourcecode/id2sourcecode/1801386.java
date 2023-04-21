    public synchronized byte[] getBytes() {
        WriteBytesChannel bc = new WriteBytesChannel();
        ChannelWriter cw = new ChannelWriter(bc);
        try {
            getChannel().Write(cw);
            cw.close();
            return bc.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
