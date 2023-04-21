    private void in2Out(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read = 0;
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
        in.close();
    }
