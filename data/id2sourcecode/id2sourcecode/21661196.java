    private void sendFile(OutputStream out, File file) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            byte[] buffer = new byte[1024];
            int bytes;
            while ((bytes = in.read(buffer)) != -1) out.write(buffer, 0, bytes);
        } finally {
            in.close();
        }
    }
