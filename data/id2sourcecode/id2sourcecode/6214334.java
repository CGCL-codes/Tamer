    private void streamToFile(InputStream in, File dest) throws IOException {
        byte[] buffer = new byte[8192];
        FileOutputStream out = new FileOutputStream(dest);
        int length;
        while ((length = in.read(buffer, 0, buffer.length)) != -1) out.write(buffer, 0, length);
        out.close();
    }
