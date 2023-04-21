    @Override
    protected void sendResponse(VirtualFile file, InputStream in) throws IOException {
        OutputStream out = response.getOutputStream();
        byte[] temp = new byte[8 * 1024];
        int read = in.read(temp);
        while (read > 0) {
            out.write(temp, 0, read);
            read = in.read(temp);
        }
    }
