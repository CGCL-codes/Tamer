    public void run() {
        byte[] buffer = new byte[512];
        int bytes_read;
        try {
            for (; ; ) {
                bytes_read = in.read(buffer);
                if (bytes_read == -1) {
                    return;
                }
                out.write(buffer, 0, bytes_read);
            }
        } catch (IOException e) {
            if (e instanceof EOFException) return; else System.out.println(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                ;
            }
        }
    }
