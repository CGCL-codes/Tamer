    @Override
    public void run() {
        while (mSocket == null) {
            try {
                if (mSocket == null) {
                    mSocket = new Socket(ip, port);
                }
            } catch (ConnectException ce) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Throwable t) {
                t.printStackTrace();
                synchronized (lastError) {
                    setLastError(t.getMessage());
                }
                return;
            }
        }
        try {
            reader = new Reader(mSocket.getInputStream());
            readerThread = new Thread(reader);
            readerThread.start();
            writer = new Writer(mSocket.getOutputStream());
            writerThread = new Thread(writer);
            writerThread.start();
        } catch (IOException e1) {
            setLastError(e1.getMessage());
            e1.printStackTrace();
        }
    }
