    public void writeResponseBody(OutputStream out, PrintWriter pw) {
        if (data != null) {
            try {
                byte[] buff = new byte[2048];
                while (true) {
                    int read = data.read(buff, 0, 2048);
                    if (read <= 0) break;
                    out.write(buff, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    data.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
