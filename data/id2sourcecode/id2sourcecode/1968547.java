    @Override
    public void run() {
        try {
            boolean firstActive = true;
            boolean secondActive = true;
            while (!Thread.currentThread().isInterrupted()) {
                if (firstActive) {
                    try {
                        output.write(first.read());
                    } catch (EOFException e) {
                        firstActive = false;
                    }
                }
                if (secondActive) {
                    try {
                        output.write(second.read());
                    } catch (EOFException e) {
                        secondActive = false;
                    }
                }
                if (!firstActive && !secondActive) {
                    break;
                }
            }
        } catch (InterruptedIOException e) {
            log.error("Merge#run()", e);
        } catch (IOException e) {
            log.error("Merge#run()", e);
        } finally {
            first.close();
            second.close();
            output.close();
        }
    }
