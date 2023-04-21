            public void run() {
                try {
                    int len;
                    byte[] buf = new byte[512];
                    while ((len = in.read(buf)) != -1) if (out != null) out.write(buf, 0, len);
                } catch (IOException e) {
                    log.warn("i/o error reading stdout/stderr of " + cmd, e);
                }
            }
