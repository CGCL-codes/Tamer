    public byte[] hash(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        algorithm.reset();
        byte[] data = new byte[2048];
        int nbRead = 0;
        while ((nbRead = bis.read(data)) > 0) {
            algorithm.update(data, 0, nbRead);
        }
        bis.close();
        bis = null;
        return algorithm.digest();
    }
