    public File getFile(long len, FileManager m) throws IOException {
        File n = m.createNewFile("cr", "get");
        if (len > MAXLONGLEN) {
            throw new IOException("Long length exceeds max! " + len);
        }
        if (len > 0) {
            FileOutputStream fos = new FileOutputStream(n);
            FileChannel foc = fos.getChannel();
            getFileChannel(foc, len);
            foc.close();
        }
        return n;
    }
