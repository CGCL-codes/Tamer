    public String getFull() throws IOException {
        byte[] buffer = new byte[4096];
        OutputStream outputStream = new ByteArrayOutputStream();
        while (true) {
            int read = this.is.read(buffer);
            if (read == -1) {
                break;
            }
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
        String s = outputStream.toString();
        return s;
    }
