    public void transcribe(Stream out) {
        try {
            byte[] buf = new byte[1024];
            while (true) {
                int numread = in.read(buf, 0, buf.length);
                if (numread == -1) return;
                out.out.write(buf, 0, numread);
            }
        } catch (IOException ioe) {
            throw new StreamException(ioe);
        }
    }
