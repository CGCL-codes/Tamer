    public void run() {
        try {
            byte[] buffer = new byte[Config.getStreamGobblerBufferSize()];
            int read;
            while (true) {
                read = 0;
                try {
                    read = is.read(buffer);
                } catch (SocketTimeoutException ste) {
                }
                if (read == -1) break;
                os.write(buffer, 0, read);
                if (stop) break;
            }
            os.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
