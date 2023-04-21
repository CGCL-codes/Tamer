    public void run() {
        while (!quit) {
            Command cmd = null;
            synchronized (buffer) {
                while (buffer.isEmpty()) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        log.severe("writerThread interrupted!");
                        break;
                    }
                }
                if (buffer.isEmpty()) {
                    break;
                }
                cmd = buffer.remove(0);
            }
            try {
                if (cmd != null) {
                    byte[] cmdBytes = cmd.getBytes();
                    StringWriter sw = new StringWriter();
                    ByteUtils.dump(cmdBytes, sw);
                    log.severe(">>> SENDING: \n" + sw.toString());
                    conn.write(cmdBytes);
                    conn.flush();
                }
            } catch (IOException e) {
                return;
            }
        }
    }
