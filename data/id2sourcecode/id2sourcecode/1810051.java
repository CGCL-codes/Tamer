    public final void run() {
        try {
            String name = readThread.getName();
            int id = System.identityHashCode(readThread);
            Logger.info("[SocketClient] STARTING " + name + " " + id);
            if (net.yura.mobile.util.QueueProcessorThread.CHANGE_PRIORITY) {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            }
            InputStream myin = in;
            try {
                while (true) {
                    if (disconnected) throw new IOException();
                    Object task = read(myin);
                    updateState(COMMUNICATING);
                    Logger.info("[SocketClient] got object: " + task + " " + id);
                    try {
                        Thread.yield();
                        Thread.sleep(0);
                        handleObject(task);
                        Thread.yield();
                        Thread.sleep(0);
                    } catch (Exception x) {
                        Logger.warn("[SocketClient] CAN NOT HANDLE! Task: " + task + " " + x.toString());
                        x.printStackTrace();
                    }
                    updateState(CONNECTED);
                }
            } catch (Exception ex) {
                Logger.info("[SocketClient] Disconnect (Exception) during read from socket " + ex.toString() + " " + id);
                boolean normal = myin == in;
                if (!(ex instanceof IOException) || (!normal && writeThread != null)) {
                    Logger.info("[SocketClient] strange disconnect in=" + in + " myin=" + myin);
                    ex.printStackTrace();
                }
                if (normal) {
                    shutdownConnection();
                }
            }
            Logger.info("[SocketClient] ENDING " + name + " " + id);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
