    private void copy(XStream in, OutputStream out) throws IOException, XAMException {
        int read;
        while ((read = (int) in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
    }
