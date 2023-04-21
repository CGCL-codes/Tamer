    public void writeTo(OutputStream out) throws IOException {
        if (root.file != null && root.buffer == null) {
            InputStream is = getInputStream();
            byte[] buff = new byte[1024];
            int ret;
            while ((ret = is.read(buff)) != -1) out.write(buff, 0, ret);
        } else out.write(array(), (int) arrayOffset(), (int) capacity());
    }
