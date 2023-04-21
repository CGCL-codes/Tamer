    public static byte[] hashFile(Digest d, File f) throws IOException, FileNotFoundException {
        byte[] buffer = new byte[65536];
        FileInputStream in = new FileInputStream(f);
        int rc = 0;
        do {
            rc = in.read(buffer);
            if (rc > 0) d.update(buffer, 0, rc);
        } while (rc != -1);
        return d.digest();
    }
