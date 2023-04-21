    public void writeContent(OutputStream oStream) throws GroomException {
        class Watchdog extends TimerTask {

            private Process _ProcessToWatch;

            Watchdog(Process iProcessToWatch) {
                _ProcessToWatch = iProcessToWatch;
            }

            public void run() {
                try {
                    _ProcessToWatch.exitValue();
                } catch (IllegalThreadStateException ex) {
                    _logger.logErr(this, 5, "CGI " + _query.Resource + " took too much time, aborting");
                    _ProcessToWatch.destroy();
                }
            }
        }
        Process aProcess = runBinary(_query.GetParameters, _query.PostParameters);
        if (_expirationTime > 0) (new Timer()).schedule(new Watchdog(aProcess), _expirationTime);
        try {
            if (_query.PostParameters != null && _query.PostParameters.length != 0) {
                OutputStream aDOS = aProcess.getOutputStream();
                aDOS.write(_query.PostParameters);
                aDOS.write("\r\n".getBytes());
                aDOS.flush();
                aDOS.close();
            }
            DataInputStream aDIS = new DataInputStream(aProcess.getInputStream());
            while (true) oStream.write(aDIS.readUnsignedByte());
        } catch (EOFException ex) {
        } catch (IOException ex) {
            throw new GroomException(GroomException.INTERNAL_ERROR);
        }
        aProcess.destroy();
        aProcess = null;
    }
