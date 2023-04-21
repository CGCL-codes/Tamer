    public void put(InputStream is) throws IOException {
        fos = new FileOutputStream(file);
        byte[] buffer = new byte[65536];
        int l;
        while ((l = is.read(buffer)) != -1) writeBuffer(buffer, l);
        fos.close();
    }
