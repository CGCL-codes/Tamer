    public void run() {
        int size = packetSize;
        byte[] content = new byte[size];
        length = 0;
        int read = 0;
        status = STARTED;
        startTime = System.currentTimeMillis();
        notifyListeners();
        try {
            do {
                read = in.read(content);
                status = PROGRESS;
                length += read;
                lastTime = System.currentTimeMillis();
                if (read > 0) {
                    out.write(content, 0, read);
                    notifyListeners();
                }
            } while (active && read > 0);
            stopTime = System.currentTimeMillis();
            in.close();
            out.close();
        } catch (IOException ioe) {
            stopTime = System.currentTimeMillis();
            status = FAILED;
            exception = ioe;
            notifyListeners();
        }
    }
