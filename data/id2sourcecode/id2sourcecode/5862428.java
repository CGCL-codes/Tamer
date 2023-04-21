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
