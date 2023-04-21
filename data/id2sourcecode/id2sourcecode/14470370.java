    public void run() {
        int nread;
        byte[] buffer = new byte[1024];
        InputStream stdout = mProcess.getInputStream();
        InputStream stderr = mProcess.getErrorStream();
        while (true) {
            try {
                if (stdout.available() > 0) {
                    nread = stdout.read(buffer);
                    mProcessStdOut.write(buffer, 0, nread);
                    mProcessStdOut.flush();
                }
                if (stderr.available() > 0) {
                    nread = stderr.read(buffer);
                    mProcessErrOut.write(buffer, 0, nread);
                    mProcessErrOut.flush();
                }
            } catch (IOException e) {
                Logger.traceException(e);
            }
            if (mHangUpFlag) break;
            try {
                Thread.sleep(mSleepTimeInMillis);
            } catch (InterruptedException e) {
            }
        }
    }
