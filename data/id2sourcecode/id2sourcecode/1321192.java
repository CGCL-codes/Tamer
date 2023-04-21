    private void dumpStream(InputStream in, OutputStream out, byte[] buffer) throws IOException {
        int numRead = 0;
        while ((numRead = in.read(buffer)) > 0) if (out != null) out.write(buffer, 0, numRead);
    }
