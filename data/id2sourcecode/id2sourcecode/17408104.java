    public static String getSHA1(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int sz;
            byte[] buf = new byte[1024];
            while ((sz = in.read(buf)) != -1) {
                buffer.write(buf, 0, sz);
            }
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(buffer.toByteArray());
            byte[] hash = md.digest();
            StringBuffer str = new StringBuffer();
            for (byte b : hash) {
                str.append(' ');
                str.append(String.format("%02x", (b & 0xff)));
            }
            return str.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("weird, everyone has this algo", e);
        } finally {
            FileUtils.close(in);
        }
    }
