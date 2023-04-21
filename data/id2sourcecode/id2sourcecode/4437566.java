    void start() throws ExpectJException {
        try {
            process.start();
            if (tm != null) tm.startTimer();
            readSystemOut = new PipedInputStream();
            writeSystemOut = new PipedOutputStream(readSystemOut);
            processOutToSystemOut = new StreamPiper(System.out, process.process.getInputStream(), writeSystemOut);
            processOutToSystemOut.start();
            readSystemErr = new PipedInputStream();
            writeSystemErr = new PipedOutputStream(readSystemErr);
            processErrToSystemErr = new StreamPiper(System.err, process.process.getErrorStream(), writeSystemErr);
            processErrToSystemErr.start();
        } catch (Exception exp) {
            throw new ExpectJException("Error in ProcessRunner.start()", exp);
        }
    }
