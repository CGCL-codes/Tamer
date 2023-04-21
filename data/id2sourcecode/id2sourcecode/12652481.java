    public byte[] encodeBinary(File file) {
        if (file == null) {
            return null;
        }
        byte[] fileDigest = null;
        try {
            if (!file.canRead()) {
                return null;
            }
            FileInputStream fis = null;
            byte[] buf = new byte[BUFFER_SIZE];
            this.md.reset();
            fis = new FileInputStream(file);
            DigestInputStream dis = new DigestInputStream(fis, this.md);
            dis.on(true);
            while (dis.read(buf, 0, BUFFER_SIZE) != -1) ;
            dis.close();
            fis.close();
            fis = null;
            fileDigest = this.md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileDigest;
    }
