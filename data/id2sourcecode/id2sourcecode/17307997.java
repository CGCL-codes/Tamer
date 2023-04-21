    public void addArchive(ZipInputStream zi) throws ZipException, IOException {
        ZipEntry ze;
        int len;
        byte buffer[] = new byte[10240];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        do {
            os.reset();
            ze = zi.getNextEntry();
            if (ze != null) {
                while ((len = zi.read(buffer, 0, buffer.length)) >= 0) os.write(buffer, 0, len);
                dataCache.put(ze.getName(), os.toByteArray());
            }
        } while (ze != null);
    }
