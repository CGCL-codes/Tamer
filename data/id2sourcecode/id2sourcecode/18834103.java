    private void startCopy(final InputStream in, final OutputStream out) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    int len;
                    byte[] buf = new byte[512];
                    while ((len = in.read(buf)) != -1) if (out != null) out.write(buf, 0, len);
                } catch (IOException e) {
                    log.warn("i/o error reading stdout/stderr of " + cmd, e);
                }
            }
        }).start();
    }
