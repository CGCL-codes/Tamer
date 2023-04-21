    public static void copy(InputStream in, OutputStream out) throws IOException {
        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[256];
                while (true) {
                    int bytesread = in.read(buffer);
                    if (bytesread == -1) {
                        break;
                    }
                    out.write(buffer, 0, bytesread);
                }
            }
        }
    }
