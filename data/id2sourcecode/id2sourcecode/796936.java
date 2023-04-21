    public void run() {
        byte[] buf = new byte[32];
        int bytes_read;
        try {
            int b = sbis.read();
            out.write(b);
            while ((bytes_read = sbis.read(buf)) != -1) out.write(buf, 0, bytes_read);
            out.close();
        } catch (IOException e) {
            System.out.println("FAILED: Basic piped stream test: " + e);
        }
    }
