    void checksum(File file, MessageDigest md) {
        try {
            FileInputStream in = new FileInputStream(file);
            byte buf[] = BufferPool.getInstance().get(1024);
            int len;
            try {
                while ((len = in.read(buf)) != -1) {
                    md.update(buf, 0, len);
                }
                byte digestBytes[] = md.digest();
                digest = Utils.formatDigest(digestBytes);
            } finally {
                BufferPool.getInstance().put(buf);
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
