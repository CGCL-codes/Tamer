    public void run() {
        int len;
        byte buf[] = new byte[128];
        try {
            String preamble = "----------- Output of stream <" + _streamName + "> for " + _cmd + " -----------\n";
            _os.write(preamble.getBytes());
            while (-1 != (len = _is.read(buf))) _os.write(buf, 0, len);
        } catch (java.io.IOException ex) {
            System.err.println(_streamName + ":" + _cmd + ": Exception " + ex);
        }
    }
