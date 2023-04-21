    @Override
    public String fileCheckSum(File f) throws IOException, NoSuchAlgorithmException {
        StringBuilder strRet = new StringBuilder();
        InputStream fin = new FileInputStream(f);
        byte[] buffer = new byte[1024];
        byte[] checksum;
        MessageDigest md = MessageDigest.getInstance(this.getAlgorithm());
        int nreaded;
        if (f.exists()) if (f.isFile()) {
            nreaded = fin.read(buffer);
            while (nreaded != -1) {
                if (nreaded > 0) md.update(buffer, 0, nreaded);
                nreaded = fin.read(buffer);
            }
            fin.close();
            checksum = md.digest();
            for (int i = 0; i < checksum.length; i++) {
                strRet.append(Integer.toString((checksum[i] & 0xff) + 0x100, 16).substring(1));
            }
        }
        return strRet.toString();
    }
