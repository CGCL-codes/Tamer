    private void forwardStream(final String name, final InputStream in, final OutputStream out) {
        new Thread("Stream forwarder [" + name + "]") {

            public void run() {
                try {
                    while (!isFinished()) {
                        while (in.available() > 0) out.write(in.read());
                        synchronized (this) {
                            this.wait(100);
                        }
                    }
                    out.flush();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }.start();
    }
