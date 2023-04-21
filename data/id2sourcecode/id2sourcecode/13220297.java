    private void sendResource() throws IOException {
        out.print(EOL);
        out.print(EOL);
        InputStream in = resource.openStream();
        byte[] buffer = new byte[4 * 1024];
        int read = -1;
        while ((read = in.read(buffer)) > -1) {
            out.write(buffer, 0, read);
        }
    }
