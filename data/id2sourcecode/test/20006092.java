    protected void loop() throws IOException, EOFException {
        BufferedInputStream bin = socket.getBufferedInputStream();
        Runnable r = getTargetReader(targetSocket, socket.getBufferedOutputStream());
        getGateway().startThread(r);
        long tstart = 0;
        long tend = 0;
        String request = null;
        String reply = null;
        int sent = 0;
        byte[] bytes = new byte[1024];
        try {
            while (alive) {
                int read = bin.read(bytes);
                printBytes("Gateway Client ", bytes, read);
                targetSocket.getOutputStream().write(bytes, 0, read);
                tend = System.currentTimeMillis();
            }
        } catch (java.net.SocketException ex) {
            if (debug) {
                System.out.println("loop(): " + ex);
            }
            handleSocketException(ex);
        } catch (EOFException ex) {
            if (debug) {
                System.out.println("loop(): " + ex);
            }
            throw ex;
        } catch (IOException ex) {
            if (debug) {
                System.out.println("loop(): " + ex);
                ex.printStackTrace();
            }
            System.out.println("loop(): " + ex);
            throw ex;
        } finally {
            if (debug) {
                System.out.println("Exiting loop(): alive = " + alive);
            }
        }
    }
