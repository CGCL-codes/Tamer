    private void forwardStream(final String name, final InputStream in, final OutputStream out) {
        new Thread("Stream forwarder [" + name + "]") {

            @Override
            public void run() {
                try {
                    while (!isFinished()) {
                        while (safeIsAvailable(in) > 0) out.write(in.read());
                        synchronized (this) {
                            this.wait(100);
                        }
                    }
                    out.flush();
                } catch (IOException e) {
                    GraphVizActivator.logUnexpected(null, e);
                } catch (InterruptedException e) {
                    GraphVizActivator.logUnexpected(null, e);
                }
            }

            private int safeIsAvailable(InputStream in) {
                try {
                    return in.available();
                } catch (IOException e) {
                    return 0;
                }
            }
        }.start();
    }
