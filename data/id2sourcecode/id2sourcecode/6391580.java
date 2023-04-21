    public void run() {
        while (!stopped) {
            try {
                readed = source.read(redirectorBuffer, 0, redirectorBufferSize);
                destination.write(redirectorBuffer, 0, readed);
                destination.flush();
            } catch (IOException e) {
                stopped = true;
                break;
            }
        }
        synchronized (session) {
            session.notify();
        }
    }
