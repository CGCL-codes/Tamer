    private void copy(InputStream str, File dest) throws IOException {
        byte[] buff = new byte[8192];
        if (dest.exists()) return;
        FileOutputStream fout = new FileOutputStream(dest);
        int read = 0;
        while ((read = str.read(buff)) != -1) {
            fout.write(buff, 0, read);
        }
        fout.flush();
        fout.close();
    }
