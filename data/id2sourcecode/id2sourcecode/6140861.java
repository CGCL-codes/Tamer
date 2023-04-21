    public byte[] getHashFromFile() throws FileNotFoundException, IOException {
        InputStream fis = null;
        try {
            fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    md.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            fis.close();
        } finally {
            fis.close();
        }
        return md.digest();
    }
