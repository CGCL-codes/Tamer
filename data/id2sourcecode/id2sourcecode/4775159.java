    public void calculateMD5Sum() throws JUploadException {
        StringBuffer ret = new StringBuffer();
        MessageDigest digest = null;
        byte md5Buffer[] = new byte[BUFLEN];
        int nbBytes;
        InputStream md5InputStream = getInputStream();
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((nbBytes = md5InputStream.read(md5Buffer, 0, BUFLEN)) > 0) {
                digest.update(md5Buffer, 0, nbBytes);
            }
        } catch (IOException e) {
            throw new JUploadIOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new JUploadException(e);
        } finally {
            try {
                md5InputStream.close();
            } catch (IOException e) {
                throw new JUploadIOException(e);
            }
        }
        byte md5sum[] = new byte[32];
        if (digest != null) md5sum = digest.digest();
        for (int i = 0; i < md5sum.length; i++) {
            ret.append(Integer.toHexString((md5sum[i] >> 4) & 0x0f));
            ret.append(Integer.toHexString(md5sum[i] & 0x0f));
        }
        this.md5sum = ret.toString();
    }
