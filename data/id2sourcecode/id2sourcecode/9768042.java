    private void copy(InputStream in, XStream out) throws IOException, XAMException {
        int read;
        while ((read = in.read(buffer)) > 0) {
            int offset = 0;
            do {
                offset += out.write(buffer, offset, read - offset);
            } while (offset < read);
        }
    }
