    protected String writeFile(File file, InputStream in) throws IOException {
        OutputStream out = null;
        out = new FileOutputStream(file);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        out = new BufferedOutputStream(out);
        in = new BufferedInputStream(in);
        byte[] buffer = new byte[4096];
        int bytes = 0;
        long total = 0;
        try {
            bytes = in.read(buffer);
            while (bytes > -1) {
                total += bytes;
                out.write(buffer, 0, bytes);
                if (md5 != null) {
                    md5.update(buffer, 0, bytes);
                }
                bytes = in.read(buffer);
            }
        } catch (IOException e1) {
            logger.error(e1);
            throw e1;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        if (md5 != null) {
            return new String(Hex.encodeHex(md5.digest()));
        } else {
            return null;
        }
    }
