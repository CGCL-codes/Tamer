    public void preloadFile(String fileName) throws IOException {
        if (fileContent != null) return;
        this.fileName = fileName;
        File f = new File(fileName);
        this.modificationDate = f.lastModified();
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[65536];
        boolean cont = true;
        while (cont) {
            int read = bis.read(buffer, 0, 65536);
            if (read == -1) cont = false; else {
                baos.write(buffer, 0, read);
            }
        }
        baos.close();
        bis.close();
        fileContent = baos.toByteArray();
    }
