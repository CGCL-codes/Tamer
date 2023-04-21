    public void write(Object o) throws IOException {
        if (isAborted(EIOEvent.WRITE)) {
            return;
        }
        if (o == null) {
            return;
        }
        boolean pauseOnWrite = false;
        synchronized (readyForWriting) {
            readyForWriting.add(o);
            if (!noWritesForYou && readyForWriting.size() > writeLimit && !directWrites) {
                numWritePauses++;
                pauseOnWrite = true;
                noWritesForYou = true;
            }
        }
        if (directWrites) {
            writeHandler.dispatch(this);
        } else {
            if (pauseOnWrite && Thread.currentThread().getThreadGroup() != context.getThreadGroup()) {
                synchronized (writeBlocker) {
                    long then = System.currentTimeMillis();
                    while (noWritesForYou) {
                        try {
                            writeBlocker.wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
            addInterest(EIOEvent.WRITE);
            coordinator.registerForEvents(this);
        }
    }
