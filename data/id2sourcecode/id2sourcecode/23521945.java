    public void run() {
        while (!stopped) {
            try {
                readChars = connection.getResultReader().read(resultBuffer, 0, resultBufferSize);
                if (readChars > 0 && !stopped) {
                    SAWTerminal.write(resultBuffer, 0, readChars);
                    SAWTerminal.flush();
                } else {
                    stopped = true;
                    break;
                }
            } catch (Exception e) {
                stopped = true;
                break;
            }
        }
        synchronized (session) {
            session.notify();
        }
    }
