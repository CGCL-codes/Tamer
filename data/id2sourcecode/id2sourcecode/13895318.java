    public void update(InputStream in, OutputStream out) {
        byte[] buff = new byte[2048];
        int len;
        try {
            while ((len = in.read(buff)) > 0) out.write(update(sub(buff, 0, len)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
