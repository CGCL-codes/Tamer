    public void run() {
        try {
            while (!stop) {
                while (is.available() > 0 && !stop) {
                    os.write(is.read());
                }
                os.flush();
                Thread.sleep(SLEEPING_TIME);
            }
        } catch (Exception e) {
            String msg = "Got exception while reading/writing the stream";
            DebugUtils.handleException(msg, e);
        } finally {
        }
    }
