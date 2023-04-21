    public void run() {
        try {
            while (!disconnecting) {
                try {
                    while (q.size() > 0 && !disconnecting) {
                        s.acquire();
                        WriteData wd = q.poll();
                        byte[] buf = wd.getData();
                        m.remove(wd.getObject());
                        s.release();
                        dos.write(buf);
                    }
                    if (!disconnecting) {
                        synchronized (this) {
                            wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dos.writeByte(Operation.clientDisconnect);
            dos.close();
            disconnected = true;
            System.out.println("disconnect message written");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("terminating tcp write thread");
    }
