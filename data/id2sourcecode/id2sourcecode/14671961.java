    public void run() {
        Log.d(TAG, Thread.currentThread().getName() + "---->" + "a client has connected to server!");
        BufferedOutputStream out;
        BufferedInputStream in;
        try {
            String currCMD = "";
            out = new BufferedOutputStream(client.getOutputStream());
            in = new BufferedInputStream(client.getInputStream());
            while (ioThreadFlag) {
                try {
                    if (!client.isConnected()) {
                        Log.v(TAG, Thread.currentThread().getName() + "---->" + "client is not Connected()");
                        break;
                    }
                    Log.v(TAG, Thread.currentThread().getName() + "---->" + "will read......");
                    byte[] returnByte = readByteFromSocket(in);
                    currCMD = readCMDFromSocket(returnByte);
                    Log.v(TAG, Thread.currentThread().getName() + "---->" + "**currCMD ==== " + currCMD);
                    out.write("OK".getBytes());
                    out.flush();
                    if (currCMD.equals("exit")) {
                        out.write("exit".getBytes());
                        out.flush();
                        ioThreadFlag = false;
                        Log.e(TAG, Thread.currentThread().getName() + "---->" + "exit");
                    } else {
                        updateText(returnByte);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ioThreadFlag = false;
                    Log.e(TAG, Thread.currentThread().getName() + "---->" + "read write error111111");
                }
            }
            out.close();
            in.close();
        } catch (Exception e) {
            Log.e(TAG, Thread.currentThread().getName() + "---->" + "read write error222222");
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    Log.v(TAG, Thread.currentThread().getName() + "---->" + "client.close()");
                    client.close();
                }
            } catch (IOException e) {
                Log.e(TAG, Thread.currentThread().getName() + "---->" + "read write error333333");
                e.printStackTrace();
            }
        }
    }
