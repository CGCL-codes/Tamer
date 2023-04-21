    public byte[] loadFileToByteArray(URI uri) throws IOException {
        File file = loadFile(uri);
        FileInputStream fis = new FileInputStream(file);
        long size = file.length();
        byte[] bt = null;
        if (size > Integer.MAX_VALUE) {
            throw new IOException("File size is greater than array upper limit(=Integer.MAX_VALUE).");
        } else {
            bt = new byte[(int) size];
            fis.read(bt);
        }
        return bt;
    }
