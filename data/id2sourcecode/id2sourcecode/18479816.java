    public static void write(InputStream input, OutputStream output, long maxLength) throws IOException {
        byte[] buffer = new byte[2048];
        int read = -1;
        int totalRead = 0;
        while ((read = input.read(buffer)) > 0) {
            totalRead += read;
            if (maxLength > -1 && totalRead > maxLength) {
                throw new IOException("Exceded max length (" + maxLength + "): " + totalRead);
            }
            output.write(buffer, 0, read);
        }
        input.close();
        output.flush();
        output.close();
    }
