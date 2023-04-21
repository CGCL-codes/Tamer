    public static String getFileMD5Sum(File f) throws IOException {
        String sum = null;
        FileInputStream in = new FileInputStream(f.getAbsolutePath());
        byte[] b = new byte[1024];
        int num = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((num = in.read(b)) != -1) {
            out.write(b, 0, num);
            if (out.size() > SCOUR_MD5_BYTE_LIMIT) {
                sum = md5Sum(out.toByteArray(), SCOUR_MD5_BYTE_LIMIT);
                break;
            }
        }
        if (sum == null) sum = md5Sum(out.toByteArray(), SCOUR_MD5_BYTE_LIMIT);
        in.close();
        out.close();
        return sum;
    }
