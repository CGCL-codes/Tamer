    private static String md5(File jarFile) {
        byte[] buf = new byte[16384];
        InputStream is = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            is = new FileInputStream(jarFile);
            int len;
            while ((len = is.read(buf)) > 0) md.update(buf, 0, len);
            return ValueUtil.toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
        } catch (IOException e) {
        } finally {
            StreamUtil.close(is);
        }
        return "";
    }
