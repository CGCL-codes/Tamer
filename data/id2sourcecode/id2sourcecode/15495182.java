    private void copyData(InputStream input, OutputStream output) throws IOException {
        logger.debug("copyData(%s, %s)", input, output);
        int len;
        byte[] buff = new byte[4096];
        while ((len = input.read(buff)) > 0) output.write(buff, 0, len);
    }
