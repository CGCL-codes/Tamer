    private void initChannel() {
        try {
            channel = bt.getChannel(address);
            dis = new DataInputStream(this.getChannel().socket().getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
