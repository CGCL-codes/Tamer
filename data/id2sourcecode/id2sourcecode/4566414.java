    public void connect() {
        try {
            this.setSockaddr(new InetSocketAddress(this.getServer(), this.getStartPort()));
            this.setSocket(new Socket());
            final int timeoutMs = 1000 * 5;
            this.setReadHandler(new IRCDataReadHandler(this));
            this.setWriteHandler(new IRCDataWriteHandler(this));
            this.getSocket().connect(this.getSockaddr(), timeoutMs);
            final Runnable runnableReader = this.getReadHandler();
            final Thread readThread = new Thread(runnableReader);
            final Runnable runnableWriter = this.getWriteHandler();
            final Thread writeThread = new Thread(runnableWriter);
            readThread.start();
            writeThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
